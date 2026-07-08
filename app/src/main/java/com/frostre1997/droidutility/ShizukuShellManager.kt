package com.frostre1997.droidutility

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"
    private const val REQUEST_CODE = 1001

    private var isBinderReceived = false
    private var isPermissionGranted = false

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        isBinderReceived = true
        Log.i(TAG, "Shizuku binder received")
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        isBinderReceived = false
        isPermissionGranted = false
        Log.w(TAG, "Shizuku binder dead")
    }

    private val permissionResultListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == REQUEST_CODE) {
            isPermissionGranted = grantResult == PackageManager.PERMISSION_GRANTED
            Log.i(TAG, "Permission result: ${if (isPermissionGranted) "granted" else "denied"}")
        }
    }

    @Suppress("PrivateApi")
    private val newProcessMethod by lazy {
        try {
            Shizuku::class.java.getDeclaredMethod(
                "newProcess",
                Array<String>::class.java,
                Array<String>::class.java,
                String::class.java
            ).apply { isAccessible = true }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get Shizuku.newProcess method", e)
            null
        }
    }

    init {
        Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
        Shizuku.addRequestPermissionResultListener(permissionResultListener)
    }

    fun checkAvailability(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
    }

    fun hasPermission(): Boolean {
        return try {
            if (!checkAvailability()) return false
            if (Shizuku.isPreV11()) return false
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    fun requestPermission(activity: Activity) {
        if (!checkAvailability()) {
            Log.w(TAG, "Shizuku not available")
            return
        }
        if (Shizuku.isPreV11()) {
            Log.w(TAG, "Shizuku pre-v11 not supported")
            return
        }
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            isPermissionGranted = true
            return
        }
        Shizuku.requestPermission(REQUEST_CODE)
    }

    fun refreshState() {
        // Force re-evaluation of listeners and state
        if (checkAvailability()) {
            isBinderReceived = true
            isPermissionGranted = hasPermission()
        } else {
            isBinderReceived = false
            isPermissionGranted = false
        }
        Log.d(TAG, "State refreshed: available=$isBinderReceived, permission=$isPermissionGranted")
    }

    suspend fun executeCommand(command: String): ShellResult {
        return withContext(Dispatchers.IO) {
            try {
                if (!checkAvailability()) {
                    return@withContext ShellResult(false, "", "Shizuku is not available", -1)
                }
                if (!hasPermission()) {
                    return@withContext ShellResult(false, "", "Shizuku permission not granted", -1)
                }
                val method = newProcessMethod ?: return@withContext ShellResult(false, "", "newProcess not available", -1)
                val process = method.invoke(null, arrayOf("sh", "-c", command), null, null) as Process
                val exitCode = process.waitFor()
                val stdout = process.inputStream.bufferedReader().readText()
                val stderr = process.errorStream.bufferedReader().readText()
                ShellResult(exitCode == 0, stdout, stderr, exitCode)
            } catch (e: Exception) {
                Log.e(TAG, "Command execution failed", e)
                ShellResult(false, "", e.message ?: "Unknown error", -1)
            }
        }
    }

    suspend fun executeCommands(commands: List<String>): List<ShellResult> {
        return commands.map { executeCommand(it) }
    }

    @Suppress("PrivateApi")
    fun startPersistentShell(): Process? {
        return try {
            if (!checkAvailability() || !hasPermission()) return null
            val method = newProcessMethod ?: return null
            val process = method.invoke(null, arrayOf("sh"), null, null) as Process
            process
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start persistent shell", e)
            null
        }
    }

    fun writeCommand(process: Process, command: String) {
        try {
            process.outputStream.write("$command\n".toByteArray())
            process.outputStream.flush()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to write command", e)
        }
    }

    data class ShellResult(
        val success: Boolean,
        val output: String,
        val error: String,
        val exitCode: Int
    )
}

fun ShizukuShellManager.ShellResult.displayText(): String {
    return buildString {
        append("Exit code: $exitCode\n\n")
        if (output.isNotBlank()) append("--- STDOUT ---\n$output\n")
        if (error.isNotBlank()) append("--- STDERR ---\n$error\n")
        if (output.isBlank() && error.isBlank()) append("(no output)")
    }
}

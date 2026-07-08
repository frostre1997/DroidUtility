package com.frostre1997.droidutility

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"
    private const val REQUEST_CODE = 1001

    // StateFlow for UI to observe
    private val _shizukuState = MutableStateFlow(ShizukuState.UNKNOWN)
    val shizukuState: StateFlow<ShizukuState> = _shizukuState

    enum class ShizukuState {
        UNKNOWN,
        NOT_AVAILABLE,
        AVAILABLE_NO_PERMISSION,
        AVAILABLE_GRANTED
    }

    private var isBinderReceived = false
    private var isPermissionGranted = false

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        isBinderReceived = true
        Log.i(TAG, "Shizuku binder received")
        updateState()
    }

    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        isBinderReceived = false
        isPermissionGranted = false
        Log.w(TAG, "Shizuku binder dead")
        updateState()
    }

    private val permissionResultListener = Shizuku.OnRequestPermissionResultListener { requestCode, grantResult ->
        if (requestCode == REQUEST_CODE) {
            isPermissionGranted = grantResult == PackageManager.PERMISSION_GRANTED
            Log.i(TAG, "Permission result: ${if (isPermissionGranted) "granted" else "denied"}")
            updateState()
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
        updateState() // initial state
    }

    private fun updateState() {
        val available = isShizukuAvailableInternal()
        val granted = if (available) isPermissionGrantedInternal() else false
        _shizukuState.value = when {
            !available -> ShizukuState.NOT_AVAILABLE
            available && !granted -> ShizukuState.AVAILABLE_NO_PERMISSION
            available && granted -> ShizukuState.AVAILABLE_GRANTED
            else -> ShizukuState.UNKNOWN
        }
    }

    private fun isShizukuAvailableInternal(): Boolean {
        return try {
            Shizuku.getVersion() != -1
        } catch (e: Exception) {
            false
        }
    }

    private fun isPermissionGrantedInternal(): Boolean {
        return try {
            if (!isShizukuAvailableInternal()) return false
            if (Shizuku.isPreV11()) return false
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    fun checkAvailability(): Boolean = _shizukuState.value == ShizukuState.AVAILABLE_GRANTED || _shizukuState.value == ShizukuState.AVAILABLE_NO_PERMISSION
    fun hasPermission(): Boolean = _shizukuState.value == ShizukuState.AVAILABLE_GRANTED

    fun requestPermission(activity: Activity) {
        if (checkAvailability() && !hasPermission()) {
            Shizuku.requestPermission(REQUEST_CODE)
        } else if (!checkAvailability()) {
            Log.w(TAG, "Shizuku not available")
        }
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

    // ─── Persistent shell support ──────────────────────────────

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

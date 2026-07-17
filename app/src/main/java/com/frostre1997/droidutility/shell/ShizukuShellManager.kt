package com.frostre1997.droidutility

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shell
import rikka.shizuku.Shizuku

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"
    private const val REQUEST_CODE = 1001

    @Volatile
    private var isBinderReceived = false
    @Volatile
    private var isPermissionGranted = false

    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        isBinderReceived = true
        isPermissionGranted = hasPermission()
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

    init {
        registerListeners()
    }

    fun registerListeners() {
        try {
            Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
            Shizuku.addBinderDeadListener(binderDeadListener)
            Shizuku.addRequestPermissionResultListener(permissionResultListener)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register Shizuku listeners", e)
        }
    }

    fun unregisterListeners() {
        try {
            Shizuku.removeBinderReceivedListener(binderReceivedListener)
            Shizuku.removeBinderDeadListener(binderDeadListener)
            Shizuku.removeRequestPermissionResultListener(permissionResultListener)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister Shizuku listeners", e)
        }
    }

    fun checkAvailability(): Boolean {
        return runCatching { Shizuku.pingBinder() }.getOrDefault(false)
    }

    fun hasPermission(): Boolean {
        if (!checkAvailability() || Shizuku.isPreV11()) return false
        return runCatching {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        }.getOrDefault(false)
    }

    // Fixed: Shizuku.requestPermission only takes an Int request code
    fun requestPermission(activity: Activity) {
        if (!checkAvailability() || Shizuku.isPreV11()) {
            Log.w(TAG, "Shizuku initialization or version check failed")
            return
        }
        if (hasPermission()) {
            isPermissionGranted = true
            return
        }
        runCatching {
            Shizuku.requestPermission(REQUEST_CODE)
        }.onFailure { e ->
            Log.e(TAG, "Failed to request permission", e)
        }
    }

    // Use Shell.run() instead of private Shizuku.newProcess
    suspend fun executeCommand(command: String): ShellResult = withContext(Dispatchers.IO) {
        if (!checkAvailability()) return@withContext ShellResult(false, "", "Shizuku is not available", -1)
        if (!hasPermission()) return@withContext ShellResult(false, "", "Shizuku permission not granted", -1)

        return@withContext runCatching {
            val result = Shell.run(command)
            ShellResult(
                success = result.isSuccess,
                output = result.out,
                error = result.err,
                exitCode = result.code
            )
        }.getOrElse { e ->
            Log.e(TAG, "Command execution failed: $command", e)
            ShellResult(false, "", e.message ?: "Unknown error", -1)
        }
    }

    suspend fun executeCommands(commands: List<String>): List<ShellResult> {
        return commands.map { executeCommand(it) }
    }

    // Persistent shell removed because Shizuku.newProcess is private.
    // Use executeCommand() for individual commands instead.
    @Deprecated("Persistent shell is not supported; use executeCommand() instead.")
    fun startPersistentShell(): Nothing? = null

    @Deprecated("Persistent shell is not supported; use executeCommand() instead.")
    fun writeCommand(process: Any, command: String) {
        // No-op
    }

    data class ShellResult(
        val success: Boolean,
        val output: String,
        val error: String,
        val exitCode: Int
    )
}

fun ShizukuShellManager.ShellResult.displayText(): String = buildString {
    append("Exit code: $exitCode\n\n")
    if (output.isNotBlank()) append("--- STDOUT ---\n$output\n")
    if (error.isNotBlank()) append("--- STDERR ---\n$error\n")
    if (output.isBlank() && error.isBlank()) append("(no output)")
}

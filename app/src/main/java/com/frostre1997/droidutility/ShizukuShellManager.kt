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
            if (Shizuku.isPreV11()) {
                false
            } else {
                Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
            }
        } catch (e: Exception) {
            false
        }
    }

    @Suppress("UNUSED_PARAMETER")
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

    suspend fun executeCommand(command: String): ShellResult {
        return withContext(Dispatchers.IO) {
            try {
                if (!checkAvailability()) {
                    return@withContext ShellResult(
                        success = false,
                        output = "",
                        error = "Shizuku is not available. Please start Shizuku.",
                        exitCode = -1
                    )
                }

                if (!hasPermission()) {
                    return@withContext ShellResult(
                        success = false,
                        output = "",
                        error = "Shizuku permission not granted.",
                        exitCode = -1
                    )
                }

                val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
                val exitCode = process.waitFor()
                val stdout = process.inputStream.bufferedReader().readText()
                val stderr = process.errorStream.bufferedReader().readText()

                ShellResult(
                    success = exitCode == 0,
                    output = stdout,
                    error = stderr,
                    exitCode = exitCode
                )
            } catch (e: Exception) {
                Log.e(TAG, "Command execution failed", e)
                ShellResult(
                    success = false,
                    output = "",
                    error = e.localizedMessage ?: "Unknown error",
                    exitCode = -1
                )
            }
        }
    }

    suspend fun executeCommands(commands: List<String>): List<ShellResult> {
        return commands.map { executeCommand(it) }
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
        if (output.isNotBlank()) {
            append("--- STDOUT ---\n$output\n")
        }
        if (error.isNotBlank()) {
            append("--- STDERR ---\n$error\n")
        }
        if (output.isBlank() && error.isBlank()) {
            append("(no output)")
        }
    }
}

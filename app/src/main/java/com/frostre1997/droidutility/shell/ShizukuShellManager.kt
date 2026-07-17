package com.frostre1997.droidutility

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

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
            Shizuku.requestPermission(activity, REQUEST_CODE)
        }.onFailure { e ->
            Log.e(TAG, "Failed to request permission", e)
        }
    }

    suspend fun executeCommand(command: String): ShellResult = withContext(Dispatchers.IO) {
        if (!checkAvailability()) return@withContext ShellResult(false, "", "Shizuku is not available", -1)
        if (!hasPermission()) return@withContext ShellResult(false, "", "Shizuku permission not granted", -1)

        return@withContext runCatching {
            // Using the official Shizuku.newProcess standard API safely
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            
            val stdoutBuilder = StringBuilder()
            val stderrBuilder = StringBuilder()

            val outReader = BufferedReader(InputStreamReader(process.inputStream))
            val errReader = BufferedReader(InputStreamReader(process.errorStream))

            // Non-blocking consumption of output buffers to prevent process freezing
            val outThread = Thread { outReader.forEachLine { stdoutBuilder.appendLine(it) } }.apply { start() }
            val errThread = Thread { errReader.forEachLine { stderrBuilder.appendLine(it) } }.apply { start() }

            val exitCode = process.waitFor()
            outThread.join()
            errThread.join()

            val stdout = stdoutBuilder.toString().trim()
            val stderr = stderrBuilder.toString().trim()

            ShellResult(exitCode == 0, stdout, stderr, exitCode)
        }.getOrElse { e ->
            Log.e(TAG, "Command execution failed: $command", e)
            ShellResult(false, "", e.message ?: "Unknown error", -1)
        }
    }

    suspend fun executeCommands(commands: List<String>): List<ShellResult> {
        return commands.map { executeCommand(it) }
    }

    fun startPersistentShell(): Process? {
        if (!checkAvailability() || !hasPermission()) return null
        return runCatching {
            Shizuku.newProcess(arrayOf("sh"), null, null)
        }.getOrNull()
    }

    fun writeCommand(process: Process, command: String) {
        runCatching {
            process.outputStream.write("$command\n".toByteArray())
            process.outputStream.flush()
        }.onFailure { e ->
            Log.e(TAG, "Failed to write command to persistent shell", e)
        }
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

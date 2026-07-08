package com.frostre1997.droidutility

import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rikka.shizuku.Shizuku

object ShizukuShellManager {
    private const val REQUEST_CODE = 1001

    // Simple functions – no StateFlow
    fun checkAvailability(): Boolean {
        return try {
            Shizuku.getVersion() != -1
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
        if (checkAvailability() && !hasPermission()) {
            Shizuku.requestPermission(REQUEST_CODE)
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
                // Use reflection or direct call – we'll use direct for simplicity
                // Note: we use the array overload
                val process = Shizuku.newProcess(arrayOf("sh", "-c", command))
                val exitCode = process.waitFor()
                val stdout = process.inputStream.bufferedReader().readText()
                val stderr = process.errorStream.bufferedReader().readText()
                ShellResult(exitCode == 0, stdout, stderr, exitCode)
            } catch (e: Exception) {
                Log.e("ShizukuShell", "Command failed", e)
                ShellResult(false, "", e.message ?: "Unknown error", -1)
            }
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

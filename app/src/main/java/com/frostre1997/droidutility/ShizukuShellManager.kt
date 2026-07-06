package com.frostre1997.droidutility

import android.app.Activity
import android.util.Log
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuShell
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"

    // Check if Shizuku is running – uses getVersion()
    fun checkAvailability(): Boolean {
        return try {
            Shizuku.getVersion() != -1
        } catch (e: Exception) {
            Log.e(TAG, "Shizuku not available", e)
            false
        }
    }

    // Check permission
    fun hasPermission(): Boolean {
        return try {
            Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    // Request permission
    fun requestPermission(activity: Activity) {
        if (checkAvailability() && !hasPermission()) {
            Shizuku.requestPermission(0)
        }
    }

    // Execute a shell command using ShizukuShell (no more newProcess issues)
    suspend fun executeCommand(command: String): ShellResult {
        return try {
            // Use ShizukuShell.exec – returns a Shell.Result object
            val result = ShizukuShell.exec(arrayOf("sh", "-c", command))
            ShellResult(
                success = result.code == 0,
                output = result.out ?: "",
                error = result.err ?: "",
                exitCode = result.code
            )
        } catch (e: Exception) {
            ShellResult(
                success = false,
                output = "",
                error = e.localizedMessage ?: "Unknown error",
                exitCode = -1
            )
        }
    }

    // Execute multiple commands
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

// Extension function to format output
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

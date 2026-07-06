package com.frostre1997.droidutility

import android.app.Activity
import android.util.Log
import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"

    /**
     * Check if Shizuku is running and connected.
     */
    fun checkAvailability(): Boolean {
        return try {
            Shizuku.ping()
        } catch (e: Exception) {
            Log.e(TAG, "Shizuku not available", e)
            false
        }
    }

    /**
     * Check if we already have Shizuku permission.
     */
    fun hasPermission(): Boolean {
        return try {
            Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Request Shizuku permission from the user.
     * Call this from an Activity context.
     */
    fun requestPermission(activity: Activity) {
        if (checkAvailability() && !hasPermission()) {
            Shizuku.requestPermission(0)
        }
    }

    /**
     * Execute a single shell command via Shizuku.
     * Returns a ShellResult containing stdout, stderr, and exit code.
     */
    suspend fun executeCommand(command: String): ShellResult {
        return try {
            // Correct way: pass an array of strings (varargs)
            val process = Shizuku.newProcess("sh", "-c", command)
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
            ShellResult(
                success = false,
                output = "",
                error = e.localizedMessage ?: "Unknown error",
                exitCode = -1
            )
        }
    }

    /**
     * Execute multiple commands sequentially.
     */
    suspend fun executeCommands(commands: List<String>): List<ShellResult> {
        return commands.map { executeCommand(it) }
    }

    /**
     * Data class for shell result.
     */
    data class ShellResult(
        val success: Boolean,
        val output: String,
        val error: String,
        val exitCode: Int
    )
}

/**
 * Extension function to format a ShellResult for display.
 * Defined at top level so it's available everywhere.
 */
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

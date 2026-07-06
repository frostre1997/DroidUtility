package com.frostre1997.droidutility

import android.app.Activity
import android.util.Log

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"

    // Always available (no Shizuku needed)
    fun checkAvailability(): Boolean = true

    // Always have permission (no Shizuku needed)
    fun hasPermission(): Boolean = true

    // No-op (no Shizuku needed)
    fun requestPermission(activity: Activity) {
        Log.i(TAG, "Running without Shizuku")
    }

    suspend fun executeCommand(command: String): ShellResult {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
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

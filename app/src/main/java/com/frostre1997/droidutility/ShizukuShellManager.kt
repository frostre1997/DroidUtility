package com.frostre1997.droidutility

import android.app.Activity
import android.util.Log
import rikka.shizuku.Shizuku
import rikka.shizuku.ShizukuShell

object ShizukuShellManager {
    private const val TAG = "ShizukuShellManager"

    fun checkAvailability(): Boolean {
        return try {
            Shizuku.getVersion() != -1
        } catch (e: Exception) {
            Log.e(TAG, "Shizuku not available", e)
            false
        }
    }

    fun hasPermission(): Boolean {
        return try {
            Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
    }

    fun requestPermission(activity: Activity) {
        if (checkAvailability() && !hasPermission()) {
            Shizuku.requestPermission(0)
        }
    }

    suspend fun executeCommand(command: String): ShellResult {
        return try {
            // ✅ Use ShizukuShell.exec – now provided by the 'shared' artifact
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

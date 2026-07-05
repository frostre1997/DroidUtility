package com.frostre1997.droidutility

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.TimeUnit

object ShizukuShellManager {

    private var isShizukuAvailable = false
    private var permissionGranted = false

    fun checkAvailability(): Boolean {
        isShizukuAvailable = try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            false
        }
        return isShizukuAvailable
    }

    fun hasPermission(): Boolean {
        permissionGranted = try {
            if (!checkAvailability()) return false
            Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            false
        }
        return permissionGranted
    }

    fun requestPermission() {
        if (!checkAvailability()) return
        try {
            if (Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Shizuku.requestPermission(0)
            }
        } catch (_: Exception) { }
    }

    fun executeCommand(command: String): ShellResult {
        if (!checkAvailability()) {
            return ShellResult(false, "", "Shizuku is not running. Start Shizuku and grant permission.")
        }

        if (!hasPermission()) {
            return ShellResult(false, "", "Shizuku permission not granted.")
        }

        return try {
            // Usiamo lo spread operator (*) per convertire l'array di Kotlin nel formato Java String[]
            val cmdArray = arrayOf("sh", "-c", command)
            val process = Shizuku.newProcess(cmdArray, null, null)

            val stdout = process.inputStream.bufferedReader().use { it.readText() }
            val stderr = process.errorStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            if (exitCode == 0 && stderr.isEmpty()) {
                ShellResult(true, stdout.trimEnd(), null)
            } else {
                ShellResult(false, stdout.trimEnd(), stderr.trimEnd().ifEmpty { "Exit code: $exitCode" })
            }
        } catch (e: Exception) {
            ShellResult(false, "", e.message ?: "Unknown error")
        }
    }

    fun executeCommands(commands: List<String>): List<ShellResult> {
        return commands.map { executeCommand(it) }
    }

    fun executeWithTimeout(command: String, timeoutMs: Long = 10000): ShellResult {
        if (!checkAvailability()) {
            return ShellResult(false, "", "Shizuku is not running.")
        }
        if (!hasPermission()) {
            return ShellResult(false, "", "Shizuku permission not granted.")
        }

        return try {
            // Stessa correzione anche qui con l'array esplicito
            val cmdArray = arrayOf("sh", "-c", command)
            val process = Shizuku.newProcess(cmdArray, null, null)
            
            val exited = process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)
            if (!exited) {
                process.destroy()
                return ShellResult(false, "", "Command timed out after ${timeoutMs}ms")
            }

            val stdout = process.inputStream.bufferedReader().use { it.readText() }
            val stderr = process.errorStream.bufferedReader().use { it.readText() }
            val exitCode = process.exitValue()

            if (exitCode == 0) {
                ShellResult(true, stdout.trimEnd(), null)
            } else {
                ShellResult(false, stdout.trimEnd(), stderr.trimEnd().ifEmpty { "Exit code: $exitCode" })
            }
        } catch (e: Exception) {
            ShellResult(false, "", e.message ?: "Unknown error")
        }
    }
}

data class ShellResult(
    val success: Boolean,
    val output: String,
    val error: String?
) {
    fun displayText(): String {
        return when {
            success && output.isNotEmpty() -> output
            success -> "Command executed successfully."
            !success && error != null -> "Error: $error"
            else -> "Unknown error occurred."
        }
    }
}


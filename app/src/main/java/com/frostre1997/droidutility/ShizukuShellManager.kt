package com.frostre1997.droidutility

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Process
import rikka.shizuku.Shizuku
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object ShizukuShellManager {

    private var isShizukuAvailable = false
    private var permissionGranted = false
    private var commandService: ICommandService? = null
    private val serviceLatch = CountDownLatch(1)

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            commandService = ICommandService.Stub.asInterface(binder)
            serviceLatch.countDown()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            commandService = null
        }
    }

    private val serviceArgs by lazy {
        Shizuku.UserServiceArgs(
            ComponentName("com.frostre1997.droidutility", CommandService::class.java.name)
        )
            .daemon(false)
            .processNameSuffix("command-service")
            .debuggable(BuildConfig.DEBUG)
            .version(BuildConfig.VERSION_CODE)
    }

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
                Shizuku.addRequestPermissionResultListener { requestCode, grantResult ->
                    if (grantResult == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        permissionGranted = true
                    }
                }
                Shizuku.requestPermission(0)
            }
        } catch (_: Exception) { }
    }

    private fun bindUserService(): Boolean {
        if (!checkAvailability() || !hasPermission()) return false
        
        return try {
            if (Shizuku.getVersion() >= 10) {
                Shizuku.bindUserService(serviceArgs, serviceConnection)
                serviceLatch.await(5, TimeUnit.SECONDS)
                commandService != null
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun unbindUserService() {
        try {
            if (Shizuku.getVersion() >= 10 && commandService != null) {
                Shizuku.unbindUserService(serviceArgs, serviceConnection, true)
                commandService = null
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
            if (commandService == null && !bindUserService()) {
                return ShellResult(false, "", "Failed to bind to command service")
            }

            val result = commandService?.exec(command) ?: return ShellResult(false, "", "Service not connected")
            
            val exitCodeMatch = "\\[exit_code=(-?\\d+)\\]".toRegex().find(result)
            val exitCode = exitCodeMatch?.groupValues?.get(1)?.toIntOrNull() ?: -1
            
            val stderrMatch = "\\[stderr\\](.*)\\[exit_code".toRegex(RegexOption.DOT_MATCHES_ALL).find(result)
            val stderrText = stderrMatch?.groupValues?.get(1)?.trimEnd() ?: ""
            
            val stdoutText = result
                .substringBefore("\n[stderr]")
                .substringBefore("\n[exit_code]")
                .trimEnd()

            if (exitCode == 0) {
                ShellResult(true, stdoutText, stderrText.ifEmpty { null })
            } else {
                ShellResult(false, stdoutText, stderrText.ifEmpty { "Exit code: $exitCode" })
            }
        } catch (e: Exception) {
            ShellResult(false, "", e.cause?.message ?: e.message ?: "Unknown error")
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
            if (commandService == null && !bindUserService()) {
                return ShellResult(false, "", "Failed to bind to command service")
            }

            val result = commandService?.exec(command) ?: return ShellResult(false, "", "Service not connected")
            
            val exitCodeMatch = "\\[exit_code=(-?\\d+)\\]".toRegex().find(result)
            val exitCode = exitCodeMatch?.groupValues?.get(1)?.toIntOrNull() ?: -1
            
            val stderrMatch = "\\[stderr\\](.*)\\[exit_code".toRegex(RegexOption.DOT_MATCHES_ALL).find(result)
            val stderrText = stderrMatch?.groupValues?.get(1)?.trimEnd() ?: ""
            
            val stdoutText = result
                .substringBefore("\n[stderr]")
                .substringBefore("\n[exit_code]")
                .trimEnd()

            if (exitCode == 0) {
                ShellResult(true, stdoutText, stderrText.ifEmpty { null })
            } else {
                ShellResult(false, stdoutText, stderrText.ifEmpty { "Exit code: $exitCode" })
            }
        } catch (e: Exception) {
            ShellResult(false, "", e.cause?.message ?: e.message ?: "Unknown error")
        }
    }

    fun cleanup() {
        unbindUserService()
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

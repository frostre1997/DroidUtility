package com.frostre1997.droidutility

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import java.io.File

@Serializable
data class DebloatConfig(
    val name: String,
    val description: String,
    val packages: List<DebloatPackage>
)

@Serializable
data class DebloatPackage(
    val pkg: String,
    val action: String // "disable" or "uninstall"
)

data class DebloatResult(
    val packageName: String,
    val action: String,
    val success: Boolean,
    val message: String
)

object DebloatEngine {

    private val json = Json { ignoreUnknownKeys = true }

    fun loadConfig(configFile: File): DebloatConfig? {
        return try {
            json.decodeFromString<DebloatConfig>(configFile.readText())
        } catch (e: Exception) {
            null
        }
    }

    fun loadConfigsFromDir(configsDir: File): List<Pair<String, DebloatConfig>> {
        if (!configsDir.exists() || !configsDir.isDirectory) return emptyList()

        return configsDir.listFiles()
            ?.filter { it.extension.lowercase() == "json" }
            ?.mapNotNull { file ->
                loadConfig(file)?.let { file.nameWithoutExtension to it }
            }
            ?: emptyList()
    }

    fun applyConfig(config: DebloatConfig, shellManager: ShizukuShellManager = ShizukuShellManager): List<DebloatResult> {
        if (!shellManager.checkAvailability()) {
            return listOf(DebloatResult("", "", false, "Shizuku is not available."))
        }

        if (!shellManager.hasPermission()) {
            return listOf(DebloatResult("", "", false, "Shizuku permission not granted."))
        }

        return config.packages.map { pkg ->
            val command = when (pkg.action.lowercase()) {
                "disable" -> "pm disable-user --user 0 ${pkg.pkg}"
                "uninstall" -> "pm uninstall --user 0 ${pkg.pkg}"
                else -> "pm disable-user --user 0 ${pkg.pkg}"
            }
            val result = shellManager.executeCommand(command)
            DebloatResult(
                packageName = pkg.pkg,
                action = pkg.action,
                success = result.success,
                message = result.displayText()
            )
        }
    }

    fun getInstalledPackages(shellManager: ShizukuShellManager = ShizukuShellManager): List<String> {
        val result = shellManager.executeCommand("pm list packages")
        if (!result.success) return emptyList()

        return result.output
            .lines()
            .filter { it.startsWith("package:") }
            .map { it.removePrefix("package:").trim() }
    }

    fun getDisabledPackages(shellManager: ShizukuShellManager = ShizukuShellManager): List<String> {
        val result = shellManager.executeCommand("pm list packages -d")
        if (!result.success) return emptyList()

        return result.output
            .lines()
            .filter { it.startsWith("package:") }
            .map { it.removePrefix("package:").trim() }
    }

    fun restorePackage(packageName: String, shellManager: ShizukuShellManager = ShizukuShellManager): DebloatResult {
        val result = shellManager.executeCommand("pm enable $packageName")
        return DebloatResult(
            packageName = packageName,
            action = "enable",
            success = result.success,
            message = result.displayText()
        )
    }
}

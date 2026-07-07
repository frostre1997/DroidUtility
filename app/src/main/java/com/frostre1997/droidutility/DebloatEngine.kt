package com.frostre1997.droidutility

import android.util.Log

data class DebloatResult(
    val packageName: String,
    val action: String,
    val success: Boolean,
    val message: String
)

object DebloatEngine {
    private const val TAG = "DebloatEngine"

    suspend fun disablePackage(
        packageName: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): DebloatResult {
        if (!shellManager.checkAvailability()) {
            return DebloatResult(packageName, "disable", false, "Shizuku is not available.")
        }
        if (!shellManager.hasPermission()) {
            return DebloatResult(packageName, "disable", false, "Shizuku permission not granted.")
        }
        val result = shellManager.executeCommand("pm disable-user --user 0 $packageName")
        return DebloatResult(
            packageName = packageName,
            action = "disable",
            success = result.success,
            message = result.displayText()
        )
    }

    suspend fun uninstallPackage(
        packageName: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): DebloatResult {
        if (!shellManager.checkAvailability()) {
            return DebloatResult(packageName, "uninstall", false, "Shizuku is not available.")
        }
        if (!shellManager.hasPermission()) {
            return DebloatResult(packageName, "uninstall", false, "Shizuku permission not granted.")
        }
        val result = shellManager.executeCommand("pm uninstall --user 0 $packageName")
        return DebloatResult(
            packageName = packageName,
            action = "uninstall",
            success = result.success,
            message = result.displayText()
        )
    }

    suspend fun enablePackage(
        packageName: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): DebloatResult {
        if (!shellManager.checkAvailability()) {
            return DebloatResult(packageName, "enable", false, "Shizuku is not available.")
        }
        if (!shellManager.hasPermission()) {
            return DebloatResult(packageName, "enable", false, "Shizuku permission not granted.")
        }
        val result = shellManager.executeCommand("pm enable $packageName")
        return DebloatResult(
            packageName = packageName,
            action = "enable",
            success = result.success,
            message = result.displayText()
        )
    }

    suspend fun getInstalledPackages(
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): List<String> {
        val result = shellManager.executeCommand("pm list packages")
        if (!result.success) return emptyList()
        return result.output
            .lines()
            .filter { it.startsWith("package:") }
            .map { it.removePrefix("package:").trim() }
    }

    suspend fun getDisabledPackages(
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): List<String> {
        val result = shellManager.executeCommand("pm list packages -d")
        if (!result.success) return emptyList()
        return result.output
            .lines()
            .filter { it.startsWith("package:") }
            .map { it.removePrefix("package:").trim() }
    }

    suspend fun isPackageInstalled(
        packageName: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): Boolean {
        val result = shellManager.executeCommand("pm path $packageName")
        return result.success && result.output.isNotBlank()
    }

    suspend fun clearAppData(
        packageName: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): DebloatResult {
        if (!shellManager.checkAvailability()) {
            return DebloatResult(packageName, "clear data", false, "Shizuku is not available.")
        }
        if (!shellManager.hasPermission()) {
            return DebloatResult(packageName, "clear data", false, "Shizuku permission not granted.")
        }
        val result = shellManager.executeCommand("pm clear $packageName")
        return DebloatResult(
            packageName = packageName,
            action = "clear data",
            success = result.success,
            message = result.displayText()
        )
    }

    suspend fun grantPermission(
        packageName: String,
        permission: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): DebloatResult {
        if (!shellManager.checkAvailability()) {
            return DebloatResult(packageName, "grant", false, "Shizuku is not available.")
        }
        if (!shellManager.hasPermission()) {
            return DebloatResult(packageName, "grant", false, "Shizuku permission not granted.")
        }
        val result = shellManager.executeCommand("pm grant $packageName $permission")
        return DebloatResult(
            packageName = packageName,
            action = "grant $permission",
            success = result.success,
            message = result.displayText()
        )
    }

    suspend fun revokePermission(
        packageName: String,
        permission: String,
        shellManager: ShizukuShellManager = ShizukuShellManager
    ): DebloatResult {
        if (!shellManager.checkAvailability()) {
            return DebloatResult(packageName, "revoke", false, "Shizuku is not available.")
        }
        if (!shellManager.hasPermission()) {
            return DebloatResult(packageName, "revoke", false, "Shizuku permission not granted.")
        }
        val result = shellManager.executeCommand("pm revoke $packageName $permission")
        return DebloatResult(
            packageName = packageName,
            action = "revoke $permission",
            success = result.success,
            message = result.displayText()
        )
    }
}

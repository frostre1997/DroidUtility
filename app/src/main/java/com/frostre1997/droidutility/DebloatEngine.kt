package com.frostre1997.droidutility

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File

// Data classes moved here (removed from MainActivity)
data class DebloatConfig(
    val name: String,
    val description: String,
    val packages: List<String>
)

data class DebloatResult(
    val packageName: String,
    val action: String,
    val success: Boolean,
    val message: String
)

object DebloatEngine {
    private const val TAG = "DebloatEngine"

    /**
     * Load all JSON config files from a directory.
     */
    suspend fun loadConfigsFromDir(dir: File): List<Pair<String, DebloatConfig>> {
        return withContext(Dispatchers.IO) {
            if (!dir.exists() || !dir.isDirectory) return@withContext emptyList()
            dir.listFiles { file ->
                file.isFile && file.extension.equals("json", ignoreCase = true)
            }?.mapNotNull { file ->
                try {
                    val json = JSONObject(file.readText())
                    val name = json.optString("name", file.nameWithoutExtension)
                    val description = json.optString("description", "")
                    val packages = json.optJSONArray("packages")?.let { arr ->
                        (0 until arr.length()).map { arr.getString(it) }
                    } ?: emptyList()
                    file.name to DebloatConfig(name, description, packages)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse ${file.name}", e)
                    null
                }
            }?.toList() ?: emptyList()
        }
    }

    /**
     * Apply a debloat configuration: uninstall or disable packages.
     * This function is suspend – call from a coroutine.
     */
    suspend fun applyConfig(config: DebloatConfig): List<DebloatResult> {
        val results = mutableListOf<DebloatResult>()
        for (pkg in config.packages) {
            // Try to uninstall (for user apps)
            val uninstallResult = ShizukuShellManager.executeCommand("pm uninstall $pkg")
            if (uninstallResult.success) {
                results.add(DebloatResult(pkg, "uninstall", true, "Uninstalled successfully"))
                continue
            }
            // If uninstall fails, try to disable (for system apps)
            val disableResult = ShizukuShellManager.executeCommand("pm disable $pkg")
            if (disableResult.success) {
                results.add(DebloatResult(pkg, "disable", true, "Disabled successfully"))
            } else {
                val errorMsg = disableResult.error.ifBlank { "Unknown error" }
                results.add(DebloatResult(pkg, "disable", false, "Failed: $errorMsg"))
            }
        }
        return results
    }
}

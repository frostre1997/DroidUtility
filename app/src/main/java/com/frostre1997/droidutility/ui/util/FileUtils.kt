package com.frostre1997.droidutility.ui.util

import android.content.Context
import android.os.Environment
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

object FileUtils {

    fun getExternalStoragePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath
    }

    fun getDownloadsPath(): String {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
    }

    fun getApkCacheDir(context: Context): File {
        val dir = File(context.cacheDir, "apk_updates")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun readFileLines(filePath: String): List<String> {
        return try {
            File(filePath).readLines()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun readAssetFile(context: Context, fileName: String): String {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            ""
        }
    }

    fun executeShellCommand(command: String): Pair<Boolean, String> {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText().trim()
            val exitCode = process.waitFor()
            Pair(exitCode == 0, output)
        } catch (e: Exception) {
            Pair(false, e.message ?: "Command execution failed")
        }
    }

    fun deleteFile(filePath: String): Boolean {
        return try {
            File(filePath).delete()
        } catch (e: Exception) {
            false
        }
    }

    fun fileExists(filePath: String): Boolean {
        return File(filePath).exists()
    }

    fun formatFileSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = bytes.toDouble()
        var unitIndex = 0
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024
            unitIndex++
        }
        return "%.1f %s".format(size, units[unitIndex])
    }
}

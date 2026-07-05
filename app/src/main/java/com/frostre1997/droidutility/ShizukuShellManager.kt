package com.frostre1997.droidutility

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShellManager {

    fun executeCommand(command: String): String {
        return try {
            if (!Shizuku.pingBinder()) return "Error: Shizuku not active."

            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            
            val output = process.inputStream.bufferedReader().use { it.readText() }
            val error = process.errorStream.bufferedReader().use { it.readText() }
            
            process.waitFor()
            
            if (error.isNotEmpty()) "Error: $error" else output
        } catch (e: Exception) {
            "Exception: ${e.message}"
        }
    }
}
    
       

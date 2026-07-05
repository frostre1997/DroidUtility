package com.frostre1997.droidutility

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShellManager {

    fun executeCommand(command: String): String {
        return try {
            if (!Shizuku.pingBinder()) {
                return "Error: Shizuku is not active."
            }

            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            
            val inputStream = BufferedReader(InputStreamReader(process.inputStream))
            val errorStream = BufferedReader(InputStreamReader(process.errorStream))
            
            val output = StringBuilder()
            
            inputStream.useLines { lines -> lines.forEach { output.append(it).append("\n") }  
            errorStream.useLines { lines -> lines.forEach { output.append("Error: ").append(it).append("\n") } }
            
            process.waitFor()
            
            if (output.isEmpty()) "Command executed (no output)." else output.toString()
            
        } catch (e: Exception) {
            "Eccezione: ${e.message}"
        }
    }
}

package com.frostre1997.droidutility

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShellManager {

    fun executeCommand(command: String): String {
        return try {
            if (!Shizuku.pingBinder()) {
                return "Error: Shizuku is not activated."
            }

            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = StringBuilder()
            var line: String?
            
            while (reader.readLine().also { line = it } != null) {
                output.append(line).append("\n")
            }
            
            process.waitFor()
            output.toString()
            
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

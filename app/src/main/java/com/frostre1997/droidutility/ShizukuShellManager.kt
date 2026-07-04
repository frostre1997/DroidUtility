package com.frostre1997.droidutility

import rikka.shizuku.Shizuku
import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShellManager {

    // Funzione per eseguire un comando shell tramite Shizuku
    fun executeCommand(command: String): String {
        return try {
            // Verifica se Shizuku è attivo
            if (!Shizuku.pingBinder()) {
                return "Error: Shizuku non è attivo."
            }

            // Crea un processo Shizuku
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            
            // Legge l'output del comando
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

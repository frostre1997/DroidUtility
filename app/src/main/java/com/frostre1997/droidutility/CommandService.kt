package com.frostre1997.droidutility

import android.content.Context
import android.os.RemoteException
import android.util.Log
import androidx.annotation.Keep
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandService : ICommandService.Stub() {

    constructor()

    @Keep
    constructor(context: Context) {
        Log.i("CommandService", "CommandService initialized")
    }

    override fun destroy() {
        Log.i("CommandService", "destroy called")
        System.exit(0)
    }

    @Throws(RemoteException::class)
    override fun exec(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", command))
            process.waitFor()

            val stdout = BufferedReader(InputStreamReader(process.inputStream))
                .use { it.readText().trimEnd() }

            val stderr = BufferedReader(InputStreamReader(process.errorStream))
                .use { it.readText().trimEnd() }

            val exitCode = process.exitValue()
            
            buildString {
                append(stdout)
                if (stderr.isNotEmpty()) {
                    append("\n[stderr]\n").append(stderr)
                }
                append("\n[exit_code=").append(exitCode).append("]")
            }
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}

package com.frostre1997.droidutility

import rikka.shizuku.Shizuku

/**
 * Custom wrapper for Shizuku shell execution.
 * Uses the public array overload of Shizuku.newProcess().
 */
object ShizukuShell {
    data class Result(val code: Int, val out: String?, val err: String?)

    @JvmStatic
    fun exec(vararg command: String): Result {
        return try {
            // Use the array overload – this is public
            val process = Shizuku.newProcess(command)
            val exitCode = process.waitFor()
            val out = process.inputStream.bufferedReader().readText()
            val err = process.errorStream.bufferedReader().readText()
            Result(exitCode, out, err)
        } catch (e: Exception) {
            Result(-1, null, e.message)
        }
    }
}

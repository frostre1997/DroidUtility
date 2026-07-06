package rikka.shizuku

import java.io.BufferedReader
import java.io.InputStreamReader

object ShizukuShell {
    data class Result(
        val code: Int,
        val out: String?,
        val err: String?
    )

    @JvmStatic
    @JvmOverloads
    fun exec(vararg command: String): Result {
        return try {
            val process = Shizuku.newProcess(*command)
            val exitCode = process.waitFor()
            val out = process.inputStream.bufferedReader().readText()
            val err = process.errorStream.bufferedReader().readText()
            Result(exitCode, out, err)
        } catch (e: Exception) {
            Result(-1, null, e.localizedMessage)
        }
    }
}

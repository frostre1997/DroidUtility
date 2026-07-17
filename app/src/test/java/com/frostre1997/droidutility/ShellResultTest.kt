package com.frostre1997.droidutility

import org.junit.Assert.*
import org.junit.Test

class ShellResultTest {

    @Test
    fun `ShellResult equality`() {
        val r1 = ShizukuShellManager.ShellResult(true, "output", "error", 0)
        val r2 = ShizukuShellManager.ShellResult(true, "output", "error", 0)
        assertEquals(r1, r2)
    }

    @Test
    fun `ShellResult inequality`() {
        val r1 = ShizukuShellManager.ShellResult(true, "out1", "", 0)
        val r2 = ShizukuShellManager.ShellResult(false, "out2", "err", 1)
        assertNotEquals(r1, r2)
    }

    @Test
    fun `displayText with stdout only`() {
        val result = ShizukuShellManager.ShellResult(true, "hello world", "", 0)
        val text = result.displayText()
        assertTrue(text.contains("Exit code: 0"))
        assertTrue(text.contains("--- STDOUT ---"))
        assertTrue(text.contains("hello world"))
        assertFalse(text.contains("--- STDERR ---"))
    }

    @Test
    fun `displayText with stderr only`() {
        val result = ShizukuShellManager.ShellResult(false, "", "permission denied", 1)
        val text = result.displayText()
        assertTrue(text.contains("Exit code: 1"))
        assertTrue(text.contains("--- STDERR ---"))
        assertTrue(text.contains("permission denied"))
        assertFalse(text.contains("--- STDOUT ---"))
    }

    @Test
    fun `displayText with both stdout and stderr`() {
        val result = ShizukuShellManager.ShellResult(false, "partial", "error msg", 2)
        val text = result.displayText()
        assertTrue(text.contains("--- STDOUT ---"))
        assertTrue(text.contains("--- STDERR ---"))
        assertTrue(text.contains("partial"))
        assertTrue(text.contains("error msg"))
    }

    @Test
    fun `displayText with no output`() {
        val result = ShizukuShellManager.ShellResult(true, "", "", 0)
        val text = result.displayText()
        assertTrue(text.contains("(no output)"))
        assertFalse(text.contains("--- STDOUT ---"))
        assertFalse(text.contains("--- STDERR ---"))
    }

    @Test
    fun `ShellResult copy works`() {
        val original = ShizukuShellManager.ShellResult(true, "data", "", 0)
        val copied = original.copy(success = false, exitCode = 1)
        assertFalse(copied.success)
        assertEquals(1, copied.exitCode)
        assertEquals("data", copied.output)
    }
}

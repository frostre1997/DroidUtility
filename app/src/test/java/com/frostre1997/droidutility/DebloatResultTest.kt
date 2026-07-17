package com.frostre1997.droidutility

import org.junit.Assert.*
import org.junit.Test

class DebloatResultTest {

    @Test
    fun `DebloatResult equality`() {
        val r1 = DebloatResult("com.test", "disable", true, "Success")
        val r2 = DebloatResult("com.test", "disable", true, "Success")
        assertEquals(r1, r2)
    }

    @Test
    fun `DebloatResult inequality`() {
        val r1 = DebloatResult("com.test", "disable", true, "OK")
        val r2 = DebloatResult("com.test", "uninstall", false, "Failed")
        assertNotEquals(r1, r2)
    }

    @Test
    fun `DebloatResult copy works`() {
        val original = DebloatResult("com.test", "disable", false, "Error")
        val copied = original.copy(success = true, message = "Fixed")
        assertTrue(copied.success)
        assertEquals("Fixed", copied.message)
        assertEquals("com.test", copied.packageName)
        assertEquals("disable", copied.action)
    }

    @Test
    fun `DebloatResult contains expected fields`() {
        val result = DebloatResult(
            packageName = "com.bloat.app",
            action = "uninstall",
            success = true,
            message = "Package removed"
        )
        assertEquals("com.bloat.app", result.packageName)
        assertEquals("uninstall", result.action)
        assertTrue(result.success)
        assertEquals("Package removed", result.message)
    }
}

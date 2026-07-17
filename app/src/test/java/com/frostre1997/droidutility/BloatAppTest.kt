package com.frostre1997.droidutility

import org.junit.Assert.*
import org.junit.Test

class BloatAppTest {

    @Test
    fun `BloatApp data class equality`() {
        val app1 = BloatApp(
            packageName = "com.test.app",
            name = "Test App",
            description = "A test app",
            category = BloatCategory.OEM_BLOATWARE
        )
        val app2 = BloatApp(
            packageName = "com.test.app",
            name = "Test App",
            description = "A test app",
            category = BloatCategory.OEM_BLOATWARE
        )
        assertEquals(app1, app2)
    }

    @Test
    fun `BloatApp data class inequality`() {
        val app1 = BloatApp(
            packageName = "com.test.app1",
            name = "Test App 1",
            description = "First",
            category = BloatCategory.OEM_BLOATWARE
        )
        val app2 = BloatApp(
            packageName = "com.test.app2",
            name = "Test App 2",
            description = "Second",
            category = BloatCategory.GAMES
        )
        assertNotEquals(app1, app2)
    }

    @Test
    fun `BloatApp default risk is SAFE`() {
        val app = BloatApp(
            packageName = "com.test",
            name = "Test",
            description = "Desc",
            category = BloatCategory.OEM_BLOATWARE
        )
        assertEquals(BloatRisk.SAFE, app.risk)
    }

    @Test
    fun `BloatApp default alternatives is empty`() {
        val app = BloatApp(
            packageName = "com.test",
            name = "Test",
            description = "Desc",
            category = BloatCategory.OEM_BLOATWARE
        )
        assertEquals("", app.alternatives)
    }

    @Test
    fun `BloatApp copy works correctly`() {
        val original = BloatApp(
            packageName = "com.test",
            name = "Test",
            description = "Desc",
            category = BloatCategory.OEM_BLOATWARE
        )
        val copied = original.copy(name = "Updated", risk = BloatRisk.ADVANCED)
        assertEquals("Updated", copied.name)
        assertEquals(BloatRisk.ADVANCED, copied.risk)
        assertEquals(original.packageName, copied.packageName)
    }

    @Test
    fun `BloatApp toString contains all fields`() {
        val app = BloatApp(
            packageName = "com.test",
            name = "Test App",
            description = "A test",
            category = BloatCategory.ADVERTISING,
            risk = BloatRisk.CAUTION,
            alternatives = "Alt1, Alt2"
        )
        val str = app.toString()
        assertTrue(str.contains("com.test"))
        assertTrue(str.contains("Test App"))
        assertTrue(str.contains("ADVERTISING"))
        assertTrue(str.contains("CAUTION"))
    }
}

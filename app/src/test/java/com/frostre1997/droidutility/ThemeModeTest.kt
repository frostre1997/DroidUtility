package com.frostre1997.droidutility

import org.junit.Assert.*
import org.junit.Test

class ThemeModeTest {

    @Test
    fun `all theme modes exist`() {
        val modes = ThemeMode.entries
        assertEquals(4, modes.size)
    }

    @Test
    fun `theme mode contains expected values`() {
        assertTrue(ThemeMode.entries.contains(ThemeMode.LIGHT))
        assertTrue(ThemeMode.entries.contains(ThemeMode.DARK))
        assertTrue(ThemeMode.entries.contains(ThemeMode.AMOLED))
        assertTrue(ThemeMode.entries.contains(ThemeMode.SYSTEM))
    }

    @Test
    fun `valueOf works for all theme modes`() {
        ThemeMode.entries.forEach { mode ->
            assertEquals(mode, ThemeMode.valueOf(mode.name))
        }
    }

    @Test
    fun `theme mode names are unique`() {
        val names = ThemeMode.entries.map { it.name }
        assertEquals(names.size, names.toSet().size)
    }
}

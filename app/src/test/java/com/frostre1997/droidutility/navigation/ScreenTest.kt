package com.frostre1997.droidutility.navigation

import org.junit.Assert.*
import org.junit.Test

class ScreenTest {

    @Test
    fun `Screen Home has correct route`() {
        assertEquals("home", Screen.Home.route)
    }

    @Test
    fun `Screen Terminal has correct route`() {
        assertEquals("terminal", Screen.Terminal.route)
    }

    @Test
    fun `Screen Debloat has correct route`() {
        assertEquals("debloat", Screen.Debloat.route)
    }

    @Test
    fun `Screen Console has correct route`() {
        assertEquals("console", Screen.Console.route)
    }

    @Test
    fun `Screen Settings has correct route`() {
        assertEquals("settings", Screen.Settings.route)
    }

    @Test
    fun `all routes are unique`() {
        val routes = listOf(
            Screen.Home.route,
            Screen.Terminal.route,
            Screen.Debloat.route,
            Screen.Console.route,
            Screen.Settings.route
        )
        assertEquals(routes.size, routes.toSet().size)
    }
}

class RoutesTest {

    @Test
    fun `Routes HOME constant is correct`() {
        assertEquals("home", Routes.HOME)
    }

    @Test
    fun `Routes CONSOLE constant is correct`() {
        assertEquals("console", Routes.CONSOLE)
    }
}

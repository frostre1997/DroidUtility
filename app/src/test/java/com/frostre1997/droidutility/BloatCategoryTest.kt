package com.frostre1997.droidutility

import org.junit.Assert.*
import org.junit.Test

class BloatCategoryTest {

    @Test
    fun `all categories exist`() {
        val categories = BloatCategory.entries
        assertEquals(10, categories.size)
    }

    @Test
    fun `category names are unique`() {
        val names = BloatCategory.entries.map { it.name }
        assertEquals(names.size, names.toSet().size)
    }

    @Test
    fun `category enum contains expected values`() {
        assertTrue(BloatCategory.entries.contains(BloatCategory.OEM_BLOATWARE))
        assertTrue(BloatCategory.entries.contains(BloatCategory.CARRIER_APPS))
        assertTrue(BloatCategory.entries.contains(BloatCategory.SOCIAL_MEDIA))
        assertTrue(BloatCategory.entries.contains(BloatCategory.GAMES))
        assertTrue(BloatCategory.entries.contains(BloatCategory.PRODUCTIVITY_BLOAT))
        assertTrue(BloatCategory.entries.contains(BloatCategory.TRACKING_SPYWARE))
        assertTrue(BloatCategory.entries.contains(BloatCategory.ADVERTISING))
        assertTrue(BloatCategory.entries.contains(BloatCategory.CLOUD_SERVICES))
        assertTrue(BloatCategory.entries.contains(BloatCategory.REDUNDANT_APPS))
        assertTrue(BloatCategory.entries.contains(BloatCategory.PRIVACY_CONCERNING))
    }

    @Test
    fun `valueOf works for all categories`() {
        BloatCategory.entries.forEach { category ->
            assertEquals(category, BloatCategory.valueOf(category.name))
        }
    }
}

class BloatRiskTest {

    @Test
    fun `all risk levels exist`() {
        val risks = BloatRisk.entries
        assertEquals(3, risks.size)
    }

    @Test
    fun `risk enum contains expected values`() {
        assertTrue(BloatRisk.entries.contains(BloatRisk.SAFE))
        assertTrue(BloatRisk.entries.contains(BloatRisk.CAUTION))
        assertTrue(BloatRisk.entries.contains(BloatRisk.ADVANCED))
    }

    @Test
    fun `valueOf works for all risks`() {
        BloatRisk.entries.forEach { risk ->
            assertEquals(risk, BloatRisk.valueOf(risk.name))
        }
    }
}

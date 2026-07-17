package com.frostre1997.droidutility

import org.junit.Assert.*
import org.junit.Test

class BloatListTest {

    @Test
    fun `BloatList ALL is not empty`() {
        assertTrue(BloatList.ALL.isNotEmpty())
    }

    @Test
    fun `BloatList ALL contains entries from all manufacturers`() {
        val all = BloatList.ALL
        val packages = all.map { it.packageName }.toSet()

        assertTrue("Should contain Samsung apps", packages.any { it.startsWith("com.samsung") })
        assertTrue("Should contain Xiaomi apps", packages.any { it.startsWith("com.miui") || it.startsWith("com.xiaomi") })
        assertTrue("Should contain OnePlus apps", packages.any { it.startsWith("com.oneplus") })
        assertTrue("Should contain Huawei apps", packages.any { it.startsWith("com.huawei") })
        assertTrue("Should contain Oppo apps", packages.any { it.startsWith("com.coloros") || it.startsWith("com.heytap") })
        assertTrue("Should contain Vivo apps", packages.any { it.startsWith("com.vivo") || it.startsWith("com.bbk") })
        assertTrue("Should contain Realme apps", packages.any { it.startsWith("com.realme") || it.startsWith("com.heytap") })
        assertTrue("Should contain Motorola apps", packages.any { it.startsWith("com.motorola") })
        assertTrue("Should contain Google apps", packages.any { it.startsWith("com.google.android") })
    }

    @Test
    fun `BloatList ALL entries have non-blank package names`() {
        BloatList.ALL.forEach { app ->
            assertTrue("Package name should not be blank: ${app.packageName}", app.packageName.isNotBlank())
        }
    }

    @Test
    fun `BloatList ALL entries have non-blank names`() {
        BloatList.ALL.forEach { app ->
            assertTrue("Name should not be blank: ${app.packageName}", app.name.isNotBlank())
        }
    }

    @Test
    fun `BloatList ALL entries have non-blank descriptions`() {
        BloatList.ALL.forEach { app ->
            assertTrue("Description should not be blank: ${app.packageName}", app.description.isNotBlank())
        }
    }

    @Test
    fun `BloatList ALL entries have valid categories`() {
        BloatList.ALL.forEach { app ->
            assertNotNull("Category should not be null: ${app.packageName}", app.category)
        }
    }

    @Test
    fun `BloatList ALL entries have valid risk levels`() {
        BloatList.ALL.forEach { app ->
            assertNotNull("Risk should not be null: ${app.packageName}", app.risk)
        }
    }

    @Test
    fun `BloatList ALL has unique package names`() {
        val packages = BloatList.ALL.map { it.packageName }
        assertEquals("Should have unique package names", packages.size, packages.toSet().size)
    }
}

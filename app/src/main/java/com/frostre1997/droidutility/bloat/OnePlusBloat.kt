package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.RiskLevel

object OnePlusBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.oneplus.brickmode",
            name = "Brick Mode",
            description = "OnePlus app locker and privacy mode",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.oneplus.gamezone",
            name = "Game Zone",
            description = "OnePlus gaming hub and performance optimizer",
            category = BloatCategory.GAMES,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.oneplus.screenshot",
            name = "OnePlus Screenshot",
            description = "Screenshot editing and sharing tool",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.oneplus.health",
            name = "OnePlus Health",
            description = "Health and fitness tracking companion",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.oneplus.logkit",
            name = "OnePlus Log Kit",
            description = "Diagnostic logging tool for OnePlus",
            category = BloatCategory.TRACKING_SPYWARE,
            risklevel = RiskLevel.CAUTION
        ),
        BloatApp(
            packageName = "com.oneplus.filemanager",
            name = "OnePlus File Manager",
            description = "Built-in file manager",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Files by Google", "Solid Explorer")
        ),
        BloatApp(
            packageName = "com.oneplus.gallery",
            name = "OnePlus Gallery",
            description = "Default gallery application",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Photos")
        ),
        BloatApp(
            packageName = "com.oneplus.cloud",
            name = "OnePlus Cloud",
            description = "OnePlus cloud backup and sync service",
            category = BloatCategory.CLOUD_SERVICES,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Drive")
        )
    )
}

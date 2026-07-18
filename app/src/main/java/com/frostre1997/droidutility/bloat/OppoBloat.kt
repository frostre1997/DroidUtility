package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.RiskLevel

object OppoBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.coloros.assistantscreen",
            name = "Smart Sidebar",
            description = "Quick access sidebar overlay",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.coloros.weather2",
            name = "Weather",
            description = "Default weather app with ads",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Weather")
        ),
        BloatApp(
            packageName = "com.heytap.browser",
            name = "OPPO Browser",
            description = "Built-in web browser",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Chrome", "Firefox")
        ),
        BloatApp(
            packageName = "com.heytap.cloud",
            name = "HeyTap Cloud",
            description = "OPPO cloud backup and sync",
            category = BloatCategory.CLOUD_SERVICES,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Drive")
        ),
        BloatApp(
            packageName = "com.heytap.market",
            name = "GetApps",
            description = "OPPO's app store",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Google Play Store")
        ),
        BloatApp(
            packageName = "com.coloros.gamespace",
            name = "Game Space",
            description = "Gaming performance optimizer",
            category = BloatCategory.GAMES,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.coloros.oshare",
            name = "OPPO Share",
            description = "File sharing between OPPO devices",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        ),
        BloatApp(
            packageName = "com.heytap.themestore",
            name = "Theme Store",
            description = "OPPO theme and wallpaper store",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE
        )
    )
}

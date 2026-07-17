package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.BloatRisk

object SamsungBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.samsung.android.app.tips",
            name = "Samsung Tips",
            description = "Samsung tips and tricks notifications",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.visionintelligence",
            name = "Bixby Vision",
            description = "Visual search and AR features powered by Bixby",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.ardrawing",
            name = "AR Drawing",
            description = "Augmented reality drawing feature",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.aremoji",
            name = "AR Emoji",
            description = "Animated emoji avatars using front camera",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.game.gamehome",
            name = "Game Launcher",
            description = "Samsung game management and optimization hub",
            category = BloatCategory.GAMES,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.app.spage",
            name = "Samsung Free",
            description = "Content aggregation and news feed",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.sec.android.app.sbrowser",
            name = "Samsung Internet",
            description = "Samsung's proprietary web browser",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Chrome, Firefox, Brave"
        ),
        BloatApp(
            packageName = "com.samsung.android.email.provider",
            name = "Samsung Email",
            description = "Samsung's email client",
            category = BloatCategory.REDUNDANT_APPS,
            risk = BloatRisk.SAFE,
            alternatives = "Gmail, Outlook"
        ),
        BloatApp(
            packageName = "com.samsung.android.app.sharelive",
            name = "Quick Share",
            description = "Samsung file sharing service",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.CAUTION
        ),
        BloatApp(
            packageName = "com.samsung.android.app.routines",
            name = "Modes and Routines",
            description = "Automation routines for Samsung devices",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.mateagent",
            name = "Galaxy Wearable",
            description = "Companion app for Galaxy Watch and Buds",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.app.talkback",
            name = "Samsung TalkBack",
            description = "Samsung's screen reader accessibility service",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.CAUTION
        ),
        BloatApp(
            packageName = "com.samsung.android.da.daagent",
            name = "Dual Messenger",
            description = "Run two instances of messaging apps",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.themestore",
            name = "Galaxy Themes",
            description = "Samsung theme and wallpaper store",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.app.sharemusic",
            name = "Music Share",
            description = "Share music playback with nearby devices",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.game.gametools",
            name = "Game Plugins",
            description = "Additional plugins for Game Launcher",
            category = BloatCategory.GAMES,
            risk = BloatRisk.SAFE
        ),
        BloatApp(
            packageName = "com.samsung.android.mobileservice",
            name = "Samsung Push Service",
            description = "Background push notification service for Samsung apps",
            category = BloatCategory.TRACKING_SPYWARE,
            risk = BloatRisk.CAUTION
        ),
        BloatApp(
            packageName = "com.samsung.android.app.galaxyfinder",
            name = "Galaxy Finder",
            description = "On-device search feature",
            category = BloatCategory.OEM_BLOATWARE,
            risk = BloatRisk.SAFE
        )
    )
}

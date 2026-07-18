package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatApp
import com.frostre1997.droidutility.BloatCategory
import com.frostre1997.droidutility.RiskLevel

object GoogleBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.google.android.googlequicksearchbox",
            name = "Google Search / Assistant",
            description = "Google search bar and voice assistant",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("None (core system app)")
        ),
        BloatApp(
            packageName = "com.google.android.apps.googleassistant",
            name = "Google Assistant",
            description = "Voice-activated AI assistant",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.youtube",
            name = "YouTube",
            description = "Google's video streaming platform",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("NewPipe", "ReVanced")
        ),
        BloatApp(
            packageName = "com.google.android.music",
            name = "YouTube Music",
            description = "Google's music streaming service",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = listOf("Spotify", "Tidal")
        ),
        BloatApp(
            packageName = "com.google.android.apps.photos",
            name = "Google Photos",
            description = "Photo backup and management service",
            category = BloatCategory.CLOUD_SERVICES,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.docs",
            name = "Google Docs",
            description = "Cloud document editor",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.slides",
            name = "Google Slides",
            description = "Cloud presentation editor",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.sheets",
            name = "Google Sheets",
            description = "Cloud spreadsheet editor",
            category = BloatCategory.REDUNDANT_APPS,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.chromecast.app",
            name = "Google Home",
            description = "Chromecast and smart home device manager",
            category = BloatCategory.OEM_BLOATWARE,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.subscriptions.red",
            name = "Google One",
            description = "Google cloud storage subscription manager",
            category = BloatCategory.CLOUD_SERVICES,
            risklevel = RiskLevel.SAFE,
            alternatives = emptyList()
        )
    )
}

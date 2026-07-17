package com.frostre1997.droidutility.bloat

import com.frostre1997.droidutility.BloatRisk
import com.frostre1997.droidutility.ui.components.BloatApp
import com.frostre1997.droidutility.ui.components.BloatCategory

object GoogleBloat {
    val BLOATWARE = listOf(
        BloatApp(
            packageName = "com.google.android.googlequicksearchbox",
            name = "Google Search / Assistant",
            description = "Google search bar and voice assistant",
            category = BloatCategory.OEM_BLOATWARE,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = listOf("None (core system app)")
        ),
        BloatApp(
            packageName = "com.google.android.apps.googleassistant",
            name = "Google Assistant",
            description = "Voice-activated AI assistant",
            category = BloatCategory.OEM_BLOATWARE,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.youtube",
            name = "YouTube",
            description = "Google's video streaming platform",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = listOf("NewPipe", "ReVanced")
        ),
        BloatApp(
            packageName = "com.google.android.music",
            name = "YouTube Music",
            description = "Google's music streaming service",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = listOf("Spotify", "Tidal")
        ),
        BloatApp(
            packageName = "com.google.android.apps.photos",
            name = "Google Photos",
            description = "Photo backup and management service",
            category = BloatCategory.CLOUD_SERVICES,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.docs",
            name = "Google Docs",
            description = "Cloud document editor",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.slides",
            name = "Google Slides",
            description = "Cloud presentation editor",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.sheets",
            name = "Google Sheets",
            description = "Cloud spreadsheet editor",
            category = BloatCategory.REDUNDANT_APPS,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.chromecast.app",
            name = "Google Home",
            description = "Chromecast and smart home device manager",
            category = BloatCategory.OEM_BLOATWARE,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        ),
        BloatApp(
            packageName = "com.google.android.apps.subscriptions.red",
            name = "Google One",
            description = "Google cloud storage subscription manager",
            category = BloatCategory.CLOUD_SERVICES,
            riskLevel = BloatRisk.SAFE,
            alternativeApps = emptyList()
        )
    )
}

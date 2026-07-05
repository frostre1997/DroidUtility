plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose") // Necessario per le nuove versioni di Compose
}

android {
    namespace = "com.frostre1997.droidutility"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.frostre1997.droidutility"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    implementation("dev.rikka.rikkax.shizuku:api:13.1.0")
    implementation("dev.rikka.rikkax.shizuku:provider:13.1.0")
}


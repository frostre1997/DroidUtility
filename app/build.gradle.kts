plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    
    // Shizuku
    implementation("dev.rikka.rikkax.shizuku:api:13.1.0")
    implementation("dev.rikka.rikkax.shizuku:provider:13.1.0")
}


plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

import java.io.ByteArrayOutputStream

// Read major/minor from gradle.properties (optional)
val versionMajor: Int = project.properties["VERSION_MAJOR"]?.toString()?.toIntOrNull() ?: 1
val versionMinor: Int = project.properties["VERSION_MINOR"]?.toString()?.toIntOrNull() ?: 0

// Auto‑increment patch using Git commit count
val versionPatch: Int = providers.exec {
    commandLine("git", "rev-list", "--count", "HEAD")
}.standardOutput.asText.get().trim().toInt()

val appVersionName = "$versionMajor.$versionMinor.$versionPatch"

android {
    namespace = "com.frostre1997.droidutility"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.frostre1997.droidutility"
        minSdk = 24
        targetSdk = 34
        versionCode = versionPatch
        versionName = appVersionName
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Shizuku API – only this, no shell module
    implementation("dev.rikka.shizuku:api:13.1.5")

    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)

    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    // Activity & Lifecycle
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Update system dependencies
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}

// Task for GitHub Actions to fetch version name
tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}

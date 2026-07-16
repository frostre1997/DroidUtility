plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// ─── Version numbers (you change these manually for each release) ───
val versionMajor = 1
val versionMinor = 0
val versionPatch = 5   // bump this for patch releases

val appVersionName = "$versionMajor.$versionMinor.$versionPatch"

// ─── Auto‑calculate version code from git tags ────────────────────────
val tagInfo = providers.exec {
    commandLine("git", "describe", "--tags", "--long")
}.standardOutput.asText.get().trim()

// Example: "v1.0.0-0-g123abc" (exactly on tag) or "v1.0.0-3-g123abc"
val parts = tagInfo.split('-')
val commitsSinceTag = parts[1].toInt()
// Version code = 1 if exactly on the tag, else commitsSinceTag + 1
val versionCodeInt = if (commitsSinceTag == 0) 1 else commitsSinceTag + 1

android {
    namespace = "com.frostre1997.droidutility"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.frostre1997.droidutility"
        minSdk = 24
        targetSdk = 34
        versionCode = versionCodeInt
        versionName = appVersionName
    }

    buildFeatures {
        compose = true
        buildConfig = true   // if you still want BuildConfig
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
    implementation("dev.rikka.shizuku:api:13.1.5")
    implementation 'dev.rikka.shizuku:provider:13.1.5'

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

// Task for GitHub Actions to fetch the version name (used for release tag)
tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}

// (Optional) Task to print version code if needed
tasks.register("printVersionCode") {
    doLast {
        println(android.defaultConfig.versionCode)
    }
}

# AGENTS.md

## Project Overview

DroidUtility is an Android system utility app that uses Shizuku (not root) for privilege elevation. Built with Kotlin, Jetpack Compose, and Material 3. Single-activity architecture with three tabs: Terminal, Debloat, and Status.

## Build & Run

```bash
./gradlew assembleDebug    # Build debug APK
./gradlew assembleRelease  # Build release APK (ProGuard enabled)
./gradlew test             # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests (requires device/emulator)
```

**Requirements:**
- JDK 17 (Temurin distribution in CI)
- Android SDK 34
- Gradle 8.5 (auto-downloaded via wrapper)

**CI:** GitHub Actions in `.github/workflows/build.yml` — builds `assembleDebug` on push/PR to `main` and uploads the APK artifact.

## Architecture

### Entry Point
`app/src/main/java/com/frostre1997/droidutility/MainActivity.kt` — the **entire app** lives in this single file. `MainActivity` extends `ComponentActivity` and sets Compose content. No fragments, no navigation graph — tab switching is a `when` block on `selectedTab` index managed by `mutableIntStateOf`.

### Core Components

| Component | File | Role |
|---|---|---|
| `ShizukuShellManager` | `ShizukuShellManager.kt` | Singleton object. Executes shell commands via Shizuku's `newProcess()`. Returns `ShellResult` data class with `success`, `output`, `error`. Has timeout-capable variant. |
| `DebloatEngine` | `DebloatEngine.kt` | Singleton object. Loads JSON debloat configs from external storage, applies disable/uninstall actions via `pm` commands, scans installed/disabled packages. Uses kotlinx.serialization. |
| `ConfigManager` | `ConfigManager.kt` | Singleton object. Loads tweaks from `assets/configs/tweaks.json` using `org.json.JSONObject` (not kotlinx.serialization). |
| `ShellResult` | `ShizukuShellManager.kt:95` | Data class with `displayText()` method for UI rendering. |
| `DebloatConfig` / `DebloatPackage` / `DebloatResult` | `DebloatEngine.kt` | Data classes for debloat config parsing and result reporting. |

### Data Flow

1. **Terminal tab:** User input → `ShizukuShellManager.executeCommand()` → `ShellResult.displayText()` → monospace text output
2. **Debloat tab:** Config files on external storage (`/storage/emulated/0/DroidUtility/configs/`) → `DebloatEngine.loadConfigsFromDir()` → user selects config → `DebloatEngine.applyConfig()` → list of `DebloatResult` → results screen
3. **Status tab:** `ShizukuShellManager.executeCommands()` with hardcoded list of system info commands → mapped labels → card-based display. Refresh button re-runs all commands.

### Dependencies

- **Shizuku API 13.1.5** (`dev.rikka.shizuku:api` + `provider`) — the core privilege mechanism
- **Compose BOM 2024.02.00** — manages Compose versions
- **kotlinx-serialization-json 1.6.2** — for debloat config parsing
- **Navigation Compose 2.7.6** — declared but **not used** (tab switching is manual, not NavHost-based)

## Key Patterns & Conventions

### No Test Files Exist
Test dependencies are declared in `build.gradle.kts` (JUnit 4, Espresso, Compose UI test) but there are **zero test source files** in the project. The `app/src/` directory has no `test/` or `androidTest/` subdirectories.

### Duplicate JSON Parsing Approaches
Two different JSON libraries are used for parsing:
- **kotlinx.serialization** (`DebloatEngine`) — for external debloat configs with `@Serializable` data classes
- **org.json** (`ConfigManager`) — for loading tweaks from assets

New JSON parsing should use **kotlinx.serialization** to stay consistent with the primary approach.

### Config File Locations & Gotchas

- **Runtime debloat configs** are loaded from **external storage**: `/storage/emulated/0/DroidUtility/configs/*.json`
- **Tweaks** are loaded from **assets**: `app/src/main/assets/configs/tweaks.json` — this directory must exist in the APK or `ConfigManager.loadTweaks()` will crash at runtime
- The `configs/` directory at the project root contains **reference/bundled** config files. The `debloat.json` there uses an **incompatible format** (packages are plain strings, not `{pkg, action}` objects). It won't parse with `DebloatEngine`. The `google-bloat.json`, `samsung-bloat.json`, and `xiaomi-bloat.json` use the correct format.

### ShizukuShellManager State Fields
The object has `isShizukuAvailable` and `permissionGranted` fields that are **written but never read** — `checkAvailability()` and `hasPermission()` always perform a fresh check. These fields are dead code.

### ProGuard Rules
The app uses `isMinifyEnabled = true` for release builds with custom ProGuard rules in `app/proguard-rules.pro`. Critical rules:
- Keep all `rikka.shizuku.**` classes
- Keep kotlinx.serialization `@Serializable` companion objects and serializers
- Keep `com.frostre1997.droidutility.**` serializer classes

**Any new `@Serializable` data class in the app package must have its serializer kept** — the existing ProGuard rules use wildcards for this.

### UI Theme
Custom dark-only theme defined inline in `MainActivity.kt` (function `DroidUtilityTheme`). No light theme support. Transparency is set on status bar and navigation bar via `themes.xml`.

### Shell Command Patterns
- Package operations use `--user 0` flag: `pm disable-user --user 0`, `pm uninstall --user 0`
- Commands are piped through `sh -c` via `Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)`
- No input stream handling — commands that require interactive input will hang or fail
- The shell scripts in `scripts/` use `#!/system/bin/sh` shebang (Android-specific)

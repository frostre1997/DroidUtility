![description](https://github.com/frostre1997/DroidUtility/blob/main/app/src/main/res/play_store_512.png)

'''don't ask me why about the image please'''

# DroidUtility

## [![Android CI](https://github.com/frostre1997/DroidUtility/actions/workflows/build.yml/badge.svg)](https://github.com/frostre1997/DroidUtility/actions/workflows/build.yml)

A powerful, non-root utility suite for Android designed for system optimization, debloating, and advanced command execution. Built for mobile-only development.

## Features

- **Shell Terminal:** Execute privileged shell commands via Shizuku with real-time output.
- **Debloat Engine:** Remove or disable unwanted system apps using JSON-based configurations. Includes presets for Samsung, Google, and Xiaomi bloatware.
- **System Status:** View device info including Android version, battery health, RAM, storage, and uptime.
- **Modular Design:** Extendable via external scripts and custom configurations.
- **Mobile-First:** Fully developed and managed on Android devices.

## Requirements

- Android 10+ (API 29)
- [Shizuku](https://shizuku.rikka.app/) installed and running
- [Termux](https://termux.dev/) for extended scripting capabilities (optional)

## How to Use

1. Install Shizuku and start it via Wireless ADB or root.
2. Install DroidUtility and grant it Shizuku permission.
3. Use the **Terminal** tab to run shell commands.
4. Use the **Debloat** tab to apply system optimization configs.
5. Use the **Status** tab to view device information.

### Custom Debloat Configs

Place JSON files in `/storage/emulated/0/DroidUtility/configs/`:

```json
{
  "name": "My Config",
  "description": "Custom debloat preset",
  "packages": [
    {"pkg": "com.example.app", "action": "disable"},
    {"pkg": "com.example.bloat", "action": "uninstall"}
  ]
}
```

### Scripts

Scripts in `scripts/` can be run via the terminal tab or Termux:

- `scan-packages.sh` — List all packages by category
- `debloat.sh` — Quick single-package disable
- `battery-optimize.sh` — Apply battery-saving system tweaks

## Building

```bash
./gradlew assembleDebug
```

Requires Android SDK 34, JDK 17, and Kotlin 1.9.21.

## License

MIT License — See [LICENSE](LICENSE) for details.

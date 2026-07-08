# 📋 Changelog – DroidUtility v1.0.1

## 🚀 Initial Stable Release

Released: July 8, 2026

This is the first stable release of DroidUtility – a powerful, non-root Android utility suite powered by Shizuku.

---

## ✨ Features

### 🖥️ Terminal Tab

· Execute any shell command via Shizuku with elevated privileges

· Real-time output display with monospace formatting

· Command history and exit code reporting

· Quick‑action buttons for common commands (pm list packages, getprop, dumpsys battery, etc.)

· Shizuku status indicator and permission request flow

---

### 🧹 Debloat Manager

· View all installed apps with their names and package IDs

· Search by app name or package name

· Filter by All / System / User apps

· Uninstall user apps with one tap

· Disable system apps (requires Shizuku)

· App icons and warning icons for system apps

· Real‑time refresh after each action

---

### 🎨 Theme Engine

· 4 theme modes: Light, Dark, AMOLED (true black), System (follows device)

· Persists across app restarts via SharedPreferences

· Material 3 design with rounded corners

---

### ⚙️ Settings Tab

· Theme switcher with visual feedback

· Shizuku status (running / permission granted)

· Autoupdate checker – detects new releases from GitHub

· Download & install new APK directly from the app

· App version display and about section

---

### 🖥️ Console Tab (Full Terminal Emulator)

· Persistent shell session – stays alive between commands

· Supports interactive commands (cd, ls, cat, nano, etc.)

· Real‑time output streaming

· Start / Close shell controls

· Type exit to gracefully close the session

· Full Shizuku integration for elevated privileges

---

### 🔧 Technical Highlights

· Shizuku integration via reflection (no external shell module required)

· No root required – works on any Android device with Shizuku

· Autoversioning – version code resets to 1 on each new release tag

· GitHub Actions CI/CD – automatic builds and releases on push to main

· Autoupdate detection – checks GitHub API for latest release

---

### 📦 Dependencies

· Kotlin 1.9.21

· Jetpack Compose (BOM 2024.02.00)

· Material 3

· Shizuku API 13.1.5

· OkHttp 4.12.0 (for update checks)

· Gson 2.10.1 (for JSON parsing)

---

### 📱 Requirements

· Android 7.0+ (API 24)

· Shizuku installed and running

· No root required

---

## 🐛 Known Issues

· Status tab has been temporarily removed (for crash) – will be re-added in a future update with a more reliable implementation (using Build class instead of shell commands).

· Shizuku must be started manually – the app cannot start Shizuku for you.

---

## 🔮 Planned for Future Releases

· Re-add Status tab with crash free implementation

· Batch operations in Debloat Manager (select multiple apps)

· App icons in Debloat list (already implemented)

· Confirmation dialogs before uninstall/disable

· Export/import configuration files

---

🌐 Website

Official website: https://frostre1997.github.io/DroidUtility/

---

🙏 Credits

· Shizuku – elevated privileges without root

· Jetpack Compose – modern UI framework

---

📄 License

MIT License – see [LICENSE](https://github.com/frostre1997/DroidUtility/blob/main/LICENSE) for details.

---

Thank you for using DroidUtility! 🤍

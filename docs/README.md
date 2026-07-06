# DroidUtility - Documentation

## Architecture
DroidUtility uses a modular architecture:
- **Shell Layer:** ShizukuShellManager handles all privileged shell execution via Shizuku API.
- **Debloat Engine:** JSON-config based system for removing/disabling packages.
- **UI Layer:** Jetpack Compose dashboard with command execution and debloat management.
- **Scripts:** External scripts for batch operations and automation.

## API Reference

### ShizukuShellManager
- `executeCommand(command: String): String` - Execute a shell command via Shizuku.
- `executeCommands(commands: List<String>): List<String>` - Execute multiple commands.
- `hasPermission(): Boolean` - Check if Shizuku permission is granted.

### DebloatEngine
- `loadConfig(configName: String): DebloatConfig` - Load a debloat configuration.
- `applyConfig(config: DebloatConfig): List<DebloatResult>` - Apply a debloat config.
- `getInstalledPackages(): List<String>` - Get list of installed packages.

## Configuration
Debloat configs are JSON files in `configs/` with the format:
```json
{
  "name": "Config Name",
  "description": "What this config does",
  "packages": [
    {"pkg": "com.example.app", "action": "disable"},
    {"pkg": "com.example.bloat", "action": "uninstall"}
  ]
}
```

## Scripts
Scripts in `scripts/` can be executed via the terminal or Termux for batch operations.

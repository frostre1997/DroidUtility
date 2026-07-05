#!/system/bin/sh
# DroidUtility - Battery Optimization
# Applies common battery-saving tweaks

echo "Applying battery optimizations..."

# Disable adaptive battery (optional)
# settings put global adaptive_battery_management_enabled 0

# Reduce animation scales
settings put global window_animation_scale 0.5
settings put global transition_animation_scale 0.5
settings put global animator_duration_scale 0.5

# Disable WiFi scanning
settings put global wifi_scan_always_enabled 0

# Disable Bluetooth scanning
settings put global ble_scan_always_enabled 0

echo "Battery optimizations applied."

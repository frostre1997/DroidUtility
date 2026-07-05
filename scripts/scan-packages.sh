#!/system/bin/sh
# DroidUtility - Package Scanner
# Lists all installed packages and categorizes them

echo "=== System Packages ==="
pm list packages -s | cut -d: -f2

echo ""
echo "=== Third-Party Packages ==="
pm list packages -3 | cut -d: -f2

echo ""
echo "=== Disabled Packages ==="
pm list packages -d | cut -d: -f2

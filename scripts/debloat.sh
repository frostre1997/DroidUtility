#!/system/bin/sh
# DroidUtility - Quick Debloat
# Usage: sh debloat.sh <package_name>

if [ -z "$1" ]; then
    echo "Usage: sh debloat.sh <package_name>"
    exit 1
fi

echo "Disabling: $1"
pm disable-user --user 0 "$1" 2>/dev/null
if [ $? -eq 0 ]; then
    echo "Successfully disabled $1"
else
    echo "Failed to disable $1 (try with Shizuku)"
fi

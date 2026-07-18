package com.frostre1997.droidutility

import android.content.pm.PackageManager
import android.util.Log
import rikka.shizuku.Shizuku

object DeblateActions {
    private const val TAG = "DebloatActions"

    fun isShizukuRunning(): Boolean {
        return try {
            Shizuku.pingBinder()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to ping Shizuku binder", e)
            false
        }
    }

    fun checkPermission(): Boolean {
        if (!isShizukuRunning()) return false
        return try {
            Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
        } catch (e: Exception) {
            Log.e(TAG, "Failed to check Shizuku permission", e)
            false
        }
    }

    fun requestPermission() {
        if (!isShizukuRunning()) {
            Log.w(TAG, "Shizuku not running, cannot request permission")
            return
        }
        try {
            Shizuku.requestPermission()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to request Shizuku permission", e)
        }
    }

    fun registerPermissionListener(listener: Shizuku.OnRequestPermissionResultListener) {
        try {
            Shizuku.addRequestPermissionResultListener(listener)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register permission listener", e)
        }
    }

    fun unregisterPermissionListener(listener: Shizuku.OnRequestPermissionResultListener) {
        try {
            Shizuku.removeRequestPermissionResultListener(listener)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to unregister permission listener", e)
        }
    }
}

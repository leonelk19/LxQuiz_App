package com.example.lxquiz.core.security

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.view.WindowManager
import com.example.lxquiz.domain.manager.ISecurityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ISecurityManager {

    private val _focusLossEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    override fun setSecureScreen(activity: Activity, isSecure: Boolean) {
        activity.runOnUiThread {
            if (isSecure) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    override fun startLockdown(activity: Activity) {
        activity.runOnUiThread {
            try {
                // Hard-coded requirement: App MUST be pinned
                activity.startLockTask()
            } catch (_: Exception) {
                // If pinning fails, we notify the system as a security risk
                notifyFocusLoss()
            }
        }
    }

    override fun stopLockdown(activity: Activity) {
        activity.runOnUiThread {
            try {
                activity.stopLockTask()
            } catch (e: Exception) {}
        }
    }

    override fun clearClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = android.content.ClipData.newPlainText("LAC_SECURE", "")
        clipboard.setPrimaryClip(clip)
    }

    override fun isDeviceSecure(): Boolean {
        return !isRooted() && !isUsbDebuggingEnabled() && !isEmulator()
    }

    private fun isRooted(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
            "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
            "/system/bin/failsafe/su", "/data/local/su"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }

    private fun isUsbDebuggingEnabled(): Boolean {
        return Settings.Global.getInt(context.contentResolver, Settings.Global.ADB_ENABLED, 0) != 0
    }

    private fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
    }

    override fun detectExternalInputDevices(): Boolean {
        val inputManager = context.getSystemService(Context.INPUT_SERVICE) as android.hardware.input.InputManager
        val deviceIds = inputManager.inputDeviceIds
        for (id in deviceIds) {
            val device = inputManager.getInputDevice(id)
            val sources = device?.sources ?: 0
            
            // Check if it's a keyboard or mouse and NOT a built-in virtual device
            val isExternalKeyboard = (sources and android.view.InputDevice.SOURCE_KEYBOARD) == android.view.InputDevice.SOURCE_KEYBOARD 
                                     && !device?.isVirtual!!
            val isExternalMouse = (sources and android.view.InputDevice.SOURCE_MOUSE) == android.view.InputDevice.SOURCE_MOUSE
                                     && !device?.isVirtual!!
            
            if (isExternalKeyboard || isExternalMouse) return true
        }
        return false
    }

    override fun detectExternalDisplays(): Boolean {
        val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as android.hardware.display.DisplayManager
        val displays = displayManager.displays
        return displays.size > 1
    }

    override fun observeFocusLoss(): Flow<Unit> = _focusLossEvents

    fun notifyFocusLoss() {
        _focusLossEvents.tryEmit(Unit)
    }
}

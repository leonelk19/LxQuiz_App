package com.example.lxquiz.domain.manager

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface ISecurityManager {
    /**
     * Prevents screenshots and screen recording.
     */
    fun setSecureScreen(activity: Activity, isSecure: Boolean)

    /**
     * Starts Lock Task Mode (Screen Pinning).
     */
    fun startLockdown(activity: Activity)

    /**
     * Stops Lock Task Mode.
     */
    fun stopLockdown(activity: Activity)

    /**
     * Clears the system clipboard to prevent copy-paste cheating.
     */
    fun clearClipboard()

    /**
     * Detects if the device is rooted or has developer options enabled.
     */
    fun isDeviceSecure(): Boolean

    /**
     * Detects when the app loses focus or enters background.
     */
    fun observeFocusLoss(): Flow<Unit>

    /**
     * Detects if external input devices (keyboard/mouse) are connected.
     */
    fun detectExternalInputDevices(): Boolean

    /**
     * Detects if the device is connected to an external display.
     */
    fun detectExternalDisplays(): Boolean
}

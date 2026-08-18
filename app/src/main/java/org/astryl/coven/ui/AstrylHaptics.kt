package org.astryl.coven.ui

import android.os.VibrationEffect
import android.os.Vibrator

class AstrylHaptics {

    // plays a sharp, precise click for gestures and keyboard taps
    fun playTapticClick(vibrator: Vibrator) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(10)
        }
    }
}
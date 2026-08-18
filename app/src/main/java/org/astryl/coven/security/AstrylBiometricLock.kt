package org.astryl.coven.security

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class AstrylBiometricLock(private val activity: FragmentActivity) {

    fun authenticate(onSuccess: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)
        
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess() // unlock the astryl ui
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                activity.finish() // close the app if they cancel or fail
            }
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("astryl node access")
            .setSubtitle("authenticate to access the sovereign matrix")
            .setNegativeButtonText("cancel")
            .build()

        val biometricPrompt = BiometricPrompt(activity, executor, callback)
        biometricPrompt.authenticate(promptInfo)
    }
}
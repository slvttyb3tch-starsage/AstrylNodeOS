package org.astryl.coven.security

import android.content.Context
import java.security.KeyStore

class AstrylDuress {

    // triggered by a duress pin or physical panic button
    fun triggerPanicWipe(context: Context) {
        // 1. wipe the local encrypted chat database
        context.deleteDatabase("astryl_chat.db")
        context.deleteDatabase("astryl_vault.db")

        // 2. destroy the master key in the secure enclave
        try {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            if (keyStore.containsAlias("astryl_master_key")) {
                keyStore.deleteEntry("astryl_master_key")
            }
        } catch (e: Exception) {
            // log failure silently
        }
    }
}
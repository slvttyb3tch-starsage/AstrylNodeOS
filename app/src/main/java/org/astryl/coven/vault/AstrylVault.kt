package org.astryl.coven.vault

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator

class AstrylVault {

    // generates and stores the master key inside the titan m2 chip
    fun generateMasterKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val spec = KeyGenParameterSpec.Builder(
            "astryl_master_key",
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setIsStrongBoxBacked(true) // forces the hardware secure enclave
            .setUserAuthenticationRequired(false)
            .build()

        keyGenerator.init(spec)
        keyGenerator.generateKey()
    }

    // retrieves the key for e2ee encryption
    fun getMasterKey(): javax.crypto.SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
        val secretKeyEntry = keyStore.getEntry("astryl_master_key", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
    }
}
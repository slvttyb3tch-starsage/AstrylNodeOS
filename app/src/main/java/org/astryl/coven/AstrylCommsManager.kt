package org.astryl.coven

import android.content.Context
import org.astryl.coven.e2ee.AstrylCipher
import org.astryl.coven.e2ee.AstrylPersistentStore
import org.astryl.coven.mesh.AstrylMeshSender
import org.signal.libsignal.protocol.SignalProtocolAddress

class AstrylCommsManager(private val context: Context) {
    private val store = AstrylPersistentStore(context)
    private val cipher = AstrylCipher(store)
    private val meshSender = AstrylMeshSender(context)

    // the complete functional flow: encrypt -> transmit
    fun sendSecureMessage(remoteDevice: android.bluetooth.BluetoothDevice, remoteName: String, plaintext: String) {
        val remoteAddress = SignalProtocolAddress(remoteName, 1)
        val plaintextBytes = plaintext.toByteArray(Charsets.UTF_8)
        
        // 1. encrypt the message using the signal protocol (pqxdh/double ratchet)
        val encryptedBytes = cipher.encryptMessage(remoteAddress, plaintextBytes)
        
        // 2. if encryption is successful, transmit over the ble mesh network
        if (encryptedBytes != null) {
            meshSender.sendEncryptedPacket(remoteDevice, encryptedBytes)
        }
    }
}
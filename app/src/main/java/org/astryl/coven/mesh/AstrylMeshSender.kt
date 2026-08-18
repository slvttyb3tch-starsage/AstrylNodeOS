package org.astryl.coven.mesh

import android.bluetooth.*
import android.content.Context
import java.util.UUID

class AstrylMeshSender(private val context: Context) {
    private val serviceUuid: UUID = UUID.fromString("0000A570-0000-1000-8000-00805F9B34FB")
    private val charUuid: UUID = UUID.fromString("0000A571-0000-1000-8000-00805F9B34FB")

    // sends the encrypted byte array to a remote astryl node
    fun sendEncryptedPacket(device: BluetoothDevice, encryptedBytes: ByteArray) {
        val gattCallback = object : BluetoothGattCallback() {
            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    val service = gatt?.getService(serviceUuid)
                    val characteristic = service?.getCharacteristic(charUuid)
                    
                    if (characteristic != null) {
                        characteristic.value = encryptedBytes
                        characteristic.writeType = BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
                        gatt.writeCharacteristic(characteristic)
                    }
                }
            }

            override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
                super.onCharacteristicWrite(gatt, characteristic, status)
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    // packet sent successfully, close the connection to save battery
                    gatt?.close()
                }
            }
        }

        // connect to the remote device and send the data
        device.connectGatt(context, false, gattCallback)
    }
}
package org.astryl.coven.mesh

import android.bluetooth.*
import android.content.Context
import java.util.UUID

class AstrylGattServer {
    private val serviceUuid: UUID = UUID.fromString("0000A570-0000-1000-8000-00805F9B34FB")
    private val charUuid: UUID = UUID.fromString("0000A571-0000-1000-8000-00805F9B34FB")
    private lateinit var gattServer: BluetoothGattServer

    fun startServer(context: Context, manager: BluetoothManager) {
        val adapter = manager.adapter
        if (adapter == null || !adapter.isMultipleAdvertisementSupported) return

        val callback = object : BluetoothGattServerCallback() {
            override fun onCharacteristicWriteRequest(device: BluetoothDevice?, requestId: Int, characteristic: BluetoothGattCharacteristic?, preparedWrite: Boolean, responseNeeded: Boolean, offset: Int, value: ByteArray?) {
                super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value)
                if (characteristic?.uuid == charUuid && value != null) {
                    // received encrypted bytes from another astryl node
                    if (responseNeeded) {
                        gattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value)
                    }
                }
            }
        }

        gattServer = manager.openGattServer(context, callback)
        
        val service = BluetoothGattService(serviceUuid, BluetoothGattService.SERVICE_TYPE_PRIMARY)
        val characteristic = BluetoothGattCharacteristic(
            charUuid,
            BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_READ,
            BluetoothGattCharacteristic.PERMISSION_WRITE or BluetoothGattCharacteristic.PERMISSION_READ
        )
        
        service.addCharacteristic(characteristic)
        gattServer.addService(service)
    }
}
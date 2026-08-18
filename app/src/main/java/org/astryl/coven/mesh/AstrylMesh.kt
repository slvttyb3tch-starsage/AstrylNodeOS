package org.astryl.coven.mesh

import android.bluetooth.le.*
import android.os.ParcelUuid

class AstrylMesh {

    // starts broadcasting the astryl mesh signal to nearby coven members
    fun startMeshAdvertising(advertiser: BluetoothLeAdvertiser) {
        val settings = AdvertiseSettings.Builder()
            .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
            .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
            .setConnectable(true)
            .build()

        val astrylUuid = ParcelUuid.fromString("0000A570-0000-1000-8000-00805F9B34FB")

        val data = AdvertiseData.Builder()
            .setIncludeDeviceName(false)
            .setIncludeTxPowerLevel(false)
            .addServiceUuid(astrylUuid)
            .build()

        val callback = object : AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {}
            override fun onStartFailure(errorCode: Int) {}
        }

        advertiser.startAdvertising(settings, data, callback)
    }
}
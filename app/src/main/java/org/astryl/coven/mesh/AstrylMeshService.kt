package org.astryl.coven.mesh

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AstrylMeshService : Service() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        
        // start the service in the foreground so grapheneos doesn't kill it to save battery
        val notification = NotificationCompat.Builder(this, "ASTRYL_MESH_CHANNEL")
            .setContentTitle("astryl node active")
            .setContentText("secure mesh network is listening...")
            .setSmallIcon(android.R.drawable.ic_dialog_info) // swap for ur actual astryl icon
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
            
        startForeground(1, notification)
        
        // TODO: initialize the ble scanner and gatt server here
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ASTRYL_MESH_CHANNEL",
                "Astryl Mesh Network",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
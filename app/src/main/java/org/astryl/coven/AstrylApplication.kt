package org.astryl.coven

import android.app.Application
import org.astryl.coven.vault.AstrylVault

class AstrylApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // initialize astryl vault on boot
        val vault = AstrylVault()
        vault.generateMasterKey()
    }
}
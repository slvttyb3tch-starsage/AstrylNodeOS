package org.astryl.coven.e2ee

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.signal.libsignal.protocol.IdentityKey
import org.signal.libsignal.protocol.IdentityKeyPair
import org.signal.libsignal.protocol.SignalProtocolAddress
import org.signal.libsignal.protocol.state.*

class AstrylPersistentStore(private val context: Context) : SessionStore, IdentityKeyStore {

    // 1. encrypted shared preferences for master identity keys
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        "astryl_identity_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // 2. sqlite database for session ratchets
    private val dbHelper = object : SQLiteOpenHelper(context, "astryl_sessions.db", null, 1) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE sessions (address TEXT PRIMARY KEY, record BLOB)")
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
    }

    // --- IdentityKeyStore implementation ---
    override fun getIdentityKeyPair(): IdentityKeyPair? {
        val serialized = prefs.getString("identity_key", null) ?: return null
        return try { IdentityKeyPair(serialized.toByteArray()) } catch (e: Exception) { null }
    }

    override fun getLocalRegistrationId(): Int {
        return prefs.getInt("registration_id", 0)
    }

    fun saveIdentity(identity: IdentityKeyPair, registrationId: Int) {
        prefs.edit()
            .putString("identity_key", String(identity.serialize()))
            .putInt("registration_id", registrationId)
            .apply()
    }

    override fun saveIdentity(address: SignalProtocolAddress?, identityKey: IdentityKey?): Boolean = true
    override fun isTrustedIdentity(address: SignalProtocolAddress?, identityKey: IdentityKey?, direction: Direction?): Boolean = true
    override fun getIdentity(address: SignalProtocolAddress?): IdentityKey? = null

    // --- SessionStore implementation ---
    override fun loadSession(address: SignalProtocolAddress?): SessionRecord? {
        if (address == null) return null
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT record FROM sessions WHERE address = ?", arrayOf(address.toString()))
        return if (cursor.moveToFirst()) {
            val recordBytes = cursor.getBlob(0)
            cursor.close()
            SessionRecord(recordBytes)
        } else {
            cursor.close()
            null
        }
    }

    override fun storeSession(address: SignalProtocolAddress?, record: SessionRecord?) {
        if (address == null || record == null) return
        val db = dbHelper.writableDatabase
        db.execSQL(
            "INSERT OR REPLACE INTO sessions (address, record) VALUES (?, ?)",
            arrayOf(address.toString(), record.serialize())
        )
    }

    override fun containsSession(address: SignalProtocolAddress?): Boolean {
        return loadSession(address) != null
    }

    override fun deleteSession(address: SignalProtocolAddress?) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM sessions WHERE address = ?", arrayOf(address?.toString()))
    }

    override fun deleteAllSessions(name: String?) {
        dbHelper.writableDatabase.execSQL("DELETE FROM sessions")
    }

    override fun getSubDeviceSessions(name: String?): List<Int> = emptyList()
}
package com.example.task

import MyMigration
import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .schemaVersion(2) // Incremented schema version
            .migration(MyMigration()) // Migration to run
            .build()
        Realm.setDefaultConfiguration(config)
    }
}

package com.example.translator_kmm.data.factory

import com.example.translator_kmm.data.source.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver


actual class DatabaseFactory {
    actual fun createDatabase(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "appDatabase.db")
    }
}
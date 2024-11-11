package com.example.translator_kmm.data.factory

import android.content.Context
import com.example.translator_kmm.data.source.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver


actual class DatabaseFactory(private val context: Context) {
    actual fun createDatabase(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "appDatabase.db")
    }
}
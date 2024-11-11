package com.example.translator_kmm.data.factory

import com.example.translator_kmm.data.model.DateAdapter
import com.example.translator_kmm.data.source.AppDatabase
import com.example.translatorkmm.data.source.LanguageEntity
import com.example.translatorkmm.data.source.TranslateEntity
import com.squareup.sqldelight.db.SqlDriver

class AppDatabaseFactory(private val databaseFactory: DatabaseFactory) {
    fun createDatabase(): AppDatabase {
        return AppDatabase(
            databaseFactory.createDatabase(),
            LanguageEntity.Adapter(
                DateAdapter(),
                DateAdapter(),
            ),
            TranslateEntity.Adapter(
                DateAdapter(),
            )
        )
    }
}

expect class DatabaseFactory {
    fun createDatabase(): SqlDriver
}

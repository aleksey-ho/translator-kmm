package com.example.translator_kmm.di

import com.example.translator_kmm.data.factory.AppDatabaseFactory
import com.example.translator_kmm.data.repository.Repository
import com.example.translator_kmm.data.repository.RepositoryImpl
import com.example.translator_kmm.data.repository.SettingsRepository
import com.example.translator_kmm.data.source.*
import com.example.translator_kmm.data.source.local.LocalDataSourceImpl
import com.example.translator_kmm.data.source.remote.RemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {

    single<TranslateMLKitManager> {
        TranslateMLKitManagerImpl()
    }

    single<AppDatabase> {
        AppDatabaseFactory(databaseFactory = get()).createDatabase()
    }

    single<SettingsRepository> {
        SettingsRepository(settings = get())
    }

    single<RemoteDataSource> {
        RemoteDataSourceImpl(translateMLKitManager = get())
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(appDatabase = get())
    }

    single<Repository> {
        RepositoryImpl(
            localDataSource = get(),
            remoteDataSource = get()
        )
    }

}


package com.example.translator_kmm.di

import android.preference.PreferenceManager
import com.example.translator_kmm.data.factory.DatabaseFactory
import com.example.translator_kmm.utils.ResourceManager
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

actual fun platformDataModule() = module {

    single<DatabaseFactory> {
        DatabaseFactory(context = get())
    }

    single<Settings> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(get())
        AndroidSettings(sharedPreferences)
    }

    single<ResourceManager> {
        ResourceManager(context = get())
    }

}



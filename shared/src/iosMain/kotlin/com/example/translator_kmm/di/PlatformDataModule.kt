package com.example.translator_kmm.di

import com.example.translator_kmm.data.factory.DatabaseFactory
import com.example.translator_kmm.utils.ResourceManager
import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual fun platformDataModule() = module {

    single<DatabaseFactory> {
        DatabaseFactory()
    }

    single<Settings> {
        AppleSettings(NSUserDefaults())
    }

    single<ResourceManager> {
        ResourceManager()
    }

}



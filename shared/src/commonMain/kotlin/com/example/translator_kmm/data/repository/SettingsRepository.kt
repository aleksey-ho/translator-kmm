package com.example.translator_kmm.data.repository

import com.russhwolf.settings.Settings

class SettingsRepository(
    val settings: Settings
) {

    fun saveBoolean(key: String, value: Boolean) {
        settings.putBoolean(key, value)
    }

    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return settings.getBoolean(key, defaultValue)
    }

    companion object {
        const val DATA_USAGE_WARNING = "DATA_USAGE_WARNING"
    }

}
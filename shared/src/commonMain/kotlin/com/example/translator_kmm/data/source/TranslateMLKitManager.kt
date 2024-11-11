package com.example.translator_kmm.data.source

interface TranslateMLKitManager {

    fun isLanguageModelDownloading(): Boolean

    fun loadRemoteLanguages(): List<String>

    suspend fun getTranslate(langSource: String, langTarget: String, text: String): String

    suspend fun downloadLanguageModel(language: String)

}

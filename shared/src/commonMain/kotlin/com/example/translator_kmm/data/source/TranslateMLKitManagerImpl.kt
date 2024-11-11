package com.example.translator_kmm.data.source


expect class TranslateMLKitManagerImpl() : TranslateMLKitManager {

    override fun isLanguageModelDownloading(): Boolean

    override fun loadRemoteLanguages(): List<String>

    override suspend fun getTranslate(langSource: String, langTarget: String, text: String): String

    override suspend fun downloadLanguageModel(language: String)

}
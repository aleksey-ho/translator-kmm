package com.example.translator_kmm.data.source

import com.example.translator_kmm.utils.MLKitManagerNotInitialized

actual class TranslateMLKitManagerImpl: TranslateMLKitManager, TranslateMLKitManagerNative {

    override var nativeDelegate: TranslateMLKitManager? = null

    actual override fun isLanguageModelDownloading(): Boolean {
        return nativeDelegate?.isLanguageModelDownloading() ?: throw MLKitManagerNotInitialized()
    }

    actual override fun loadRemoteLanguages(): List<String> {
        return nativeDelegate?.loadRemoteLanguages() ?: throw MLKitManagerNotInitialized()
    }

    actual override suspend fun getTranslate(
        langSource: String,
        langTarget: String,
        text: String
    ): String {
        return nativeDelegate?.getTranslate(langSource, langTarget, text) ?: throw MLKitManagerNotInitialized()
    }

    actual override suspend fun downloadLanguageModel(language: String) {
        nativeDelegate?.downloadLanguageModel(language) ?: throw MLKitManagerNotInitialized()
    }

}
package com.example.translator_kmm.data.source.remote

import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.data.source.RemoteDataSource
import com.example.translator_kmm.data.source.TranslateMLKitManager
import com.example.translator_kmm.utils.DownloadInProgressException
import kotlinx.datetime.Clock


class RemoteDataSourceImpl(
    val translateMLKitManager: TranslateMLKitManager
) : RemoteDataSource {

    override suspend fun getTranslate(
        langSource: Language,
        langTarget: Language,
        text: String
    ): Translate {
        if (translateMLKitManager.isLanguageModelDownloading()) {
            throw DownloadInProgressException()
        }
        if (text.isEmpty()) {
            return Translate(
                id = -1,
                languageSource = langSource,
                languageTarget = langTarget,
                date = Clock.System.now(),
                textSource = text,
                textTarget = "",
                savedInHistory = false,
                savedInFavorites = false
            )
        }
        val translate = translateMLKitManager.getTranslate(
            langSource = langSource.code,
            langTarget = langTarget.code,
            text = text
        )
        return Translate(
            id = -1,
            languageSource = langSource,
            languageTarget = langTarget,
            date = Clock.System.now(),
            textSource = text,
            textTarget = translate,
            savedInHistory = false,
            savedInFavorites = false
        )
    }

    override fun loadRemoteLanguages(): List<String> {
        return translateMLKitManager.loadRemoteLanguages()
    }

    // Starts downloading a remote model for local translation.
    override suspend fun downloadLanguageModel(language: Language) {
        translateMLKitManager.downloadLanguageModel(language.code)
    }

}
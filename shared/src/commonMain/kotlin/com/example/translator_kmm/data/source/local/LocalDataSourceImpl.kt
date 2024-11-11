package com.example.translator_kmm.data.source.local

import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.data.source.AppDatabase
import com.example.translator_kmm.data.source.LocalDataSource
import com.example.translatorkmm.data.source.LanguageEntity
import com.example.translatorkmm.data.source.TranslateEntity
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock


class LocalDataSourceImpl(
    appDatabase: AppDatabase
) : LocalDataSource {

    private val languageDao = appDatabase.languageQueries
    private val translateDao = appDatabase.translateQueries

    override suspend fun getLanguages(): List<LanguageEntity> {
        return languageDao.getLanguages().executeAsList()
    }

    override suspend fun getLanguage(code: String): LanguageEntity? {
        return languageDao.getLanguage(code).executeAsOneOrNull()
    }

    override suspend fun getRecentlyUsedSourceLanguage(): LanguageEntity? {
        return languageDao.getRecentlyUsedSourceLanguage().executeAsOneOrNull()
    }

    override suspend fun getRecentlyUsedTargetLanguage(): LanguageEntity? {
        return languageDao.getRecentlyUsedTargetLanguage().executeAsOneOrNull()
    }

    override suspend fun getRecentlyUsedSourceLanguages(): List<LanguageEntity> {
        return languageDao.getRecentlyUsedSourceLanguages().executeAsList()
    }

    override suspend fun getRecentlyUsedTargetLanguages(): List<LanguageEntity> {
        return languageDao.getRecentlyUsedTargetLanguages().executeAsList()
    }

    override suspend fun updateLanguages(list: List<String>) {
        for (code in list) {
            val language = getLanguage(code)
            if (language == null) {
                languageDao.insertLanguage(
                    code = code,
                    sourceLastUseDate = Clock.System.now(),
                    targetLastUseDate = Clock.System.now(),
                    usageCounter = 0
                )
            }
        }
    }

    override suspend fun updateLanguageUsage(
        language: Language,
        direction: LangDirection
    ) {
        languageDao.update(
            code = language.code,
            sourceLastUseDate = if (direction == LangDirection.SOURCE) {
                Clock.System.now()
            } else {
                language.sourceLastUseDate
            },
            targetLastUseDate = if (direction == LangDirection.TARGET) {
                Clock.System.now()
            } else {
                language.targetLastUseDate
            },
            usageCounter = language.usageCounter + 1
        )
    }

    override suspend fun initFirstSelectedLanguages() {
        getLanguage("en")?.let { languageSource ->
            languageDao.update(
                code = languageSource.code,
                sourceLastUseDate = Clock.System.now(),
                targetLastUseDate = languageSource.targetLastUseDate,
                usageCounter = languageSource.usageCounter
            )
        }
        getLanguage("es")?.let { languageTarget ->
            languageDao.update(
                code = languageTarget.code,
                sourceLastUseDate = languageTarget.sourceLastUseDate,
                targetLastUseDate = Clock.System.now(),
                usageCounter = languageTarget.usageCounter
            )
        }
    }

    override val translatesInHistory: Flow<List<TranslateEntity>>
        get() = translateDao.getHistory()
            .asFlow()
            .mapToList()

    override val favoriteTranslates: Flow<List<TranslateEntity>>
        get() = translateDao.getFavorites()
            .asFlow()
            .mapToList()

    override suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: Language,
        languageTarget: Language
    ) {
        if (textSource.isEmpty()) {
            return
        }
        val translate =
            translateDao.getTranslate(languageSource.code, languageTarget.code, textSource).executeAsOneOrNull()
        if (translate == null) {
            translateDao.insert(
                languageSourceCode = languageSource.code,
                languageTargetCode = languageTarget.code,
                date = Clock.System.now(),
                textSource = textSource,
                textTarget = textTranslate,
                savedInHistory = true,
                savedInFavorites = false,
            )
        } else {
            translateDao.update(
                id = translate.id,
                date = Clock.System.now(),
                savedInHistory = true,
                savedInFavorites = translate.savedInFavorites,
                textTarget = textTranslate,
            )
        }
    }

    override suspend fun saveAsFavorite(translate: Translate) {
        println("111saveAsFavorite")
        translate.savedInFavorites = true
        translate.date = Clock.System.now()
        translateDao.update(
            id = translate.id,
            savedInHistory = translate.savedInHistory,
            savedInFavorites = translate.savedInFavorites,
            date = translate.date,
            textTarget = translate.textTarget
        )
    }

    override suspend fun removeFromFavorites(translate: Translate) {
        translate.savedInFavorites = false
        translate.date = Clock.System.now()
        translateDao.update(
            id = translate.id,
            savedInHistory = translate.savedInHistory,
            savedInFavorites = translate.savedInFavorites,
            date = translate.date,
            textTarget = translate.textTarget
        )
    }

    override suspend fun clearHistory() {
        translateDao.clearHistory()
        removeRedundantEntries()
    }

    override suspend fun clearFavorites() {
        translateDao.clearFavorites()
        removeRedundantEntries()
    }

    private suspend fun removeRedundantEntries() {
        translateDao.removeRedundantEntries()
    }

}

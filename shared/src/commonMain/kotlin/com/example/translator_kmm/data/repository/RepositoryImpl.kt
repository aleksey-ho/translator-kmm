package com.example.translator_kmm.data.repository

import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.data.mapper.plain
import com.example.translator_kmm.data.mapper.plainLanguageEntityList
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.data.source.LocalDataSource
import com.example.translator_kmm.data.source.RemoteDataSource
import com.example.translatorkmm.data.source.TranslateEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository, KoinComponent {

    private val cachedTranslates: MutableList<Translate> = ArrayList()

    override suspend fun getTranslate(
        langSource: Language,
        langTarget: Language,
        text: String
    ): Translate {
        val item = cachedTranslates.firstOrNull {
            it.textSource == text && langSource == it.languageSource && langTarget == it.languageTarget
        }
        return if (item != null) {
            item
        } else {
            val translate = remoteDataSource.getTranslate(langSource, langTarget, text)
            cachedTranslates.add(translate)
            translate
        }
    }

    override suspend fun getLanguages(): List<Language> {
        return localDataSource.getLanguages().plainLanguageEntityList()
    }

    override suspend fun loadLanguages() {
        val languages = remoteDataSource.loadRemoteLanguages()
        localDataSource.updateLanguages(languages)
        localDataSource.initFirstSelectedLanguages()
    }

    override suspend fun updateLanguages(list: List<String>) {
        return localDataSource.updateLanguages(list)
    }

    override suspend fun getRecentlyUsedSourceLanguage(): Language? {
        return localDataSource.getRecentlyUsedSourceLanguage()?.plain()
    }

    override suspend fun getRecentlyUsedTargetLanguage(): Language? {
        return localDataSource.getRecentlyUsedTargetLanguage()?.plain()
    }

    override suspend fun getRecentlyUsedSourceLanguages(): List<Language> {
        return localDataSource.getRecentlyUsedSourceLanguages().plainLanguageEntityList()
    }

    override suspend fun getRecentlyUsedTargetLanguages(): List<Language> {
        return localDataSource.getRecentlyUsedTargetLanguages().plainLanguageEntityList()
    }

    override suspend fun updateLanguageUsage(language: Language, direction: LangDirection) {
        return localDataSource.updateLanguageUsage(language, direction)
    }

    override suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: Language,
        languageTarget: Language
    ) {
        localDataSource.addTranslate(
            textSource,
            textTranslate,
            languageSource,
            languageTarget
        )
    }

    override val translatesInHistory: Flow<List<Translate>>
        get() = localDataSource.translatesInHistory.map {
            it.mapNotNull {
                getPlainTranslateEntity(it)
            }
        }

    override val favoriteTranslates: Flow<List<Translate>>
        get() = localDataSource.favoriteTranslates.map {
            println("favoriteTranslates map count" + it.size)
            it.mapNotNull {
                println("it.savedInFavorites = " + it.savedInFavorites)
                getPlainTranslateEntity(it)
            }
        }

    private suspend fun getPlainTranslateEntity(entity: TranslateEntity): Translate? {
        return Translate(
            id = entity.id,
            languageSource = localDataSource.getLanguage(entity.languageSourceCode)?.plain()
                ?: return null,
            languageTarget = localDataSource.getLanguage(entity.languageTargetCode)?.plain()
                ?: return null,
            date = entity.date,
            textSource = entity.textSource,
            textTarget = entity.textTarget,
            savedInHistory = entity.savedInHistory,
            savedInFavorites = entity.savedInFavorites,
        )
    }

    override suspend fun saveAsFavorite(translate: Translate) {
        return localDataSource.saveAsFavorite(translate)
    }

    override suspend fun removeFromFavorites(translate: Translate) {
        return localDataSource.removeFromFavorites(translate)
    }

    override suspend fun clearHistory() {
        localDataSource.clearHistory()
    }

    override suspend fun clearFavorites() {
        localDataSource.clearFavorites()
    }

    override suspend fun downloadLanguageModels() {
        localDataSource.getRecentlyUsedSourceLanguage()?.plain()?.let {
            downloadLanguageModel(it)
        }
        localDataSource.getRecentlyUsedTargetLanguage()?.plain()?.let {
            downloadLanguageModel(it)
        }
    }

    override suspend fun downloadLanguageModel(language: Language) {
        remoteDataSource.downloadLanguageModel(language)
    }

}
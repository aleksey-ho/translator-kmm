package com.example.translator_kmm.data.source

import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translatorkmm.data.source.LanguageEntity
import com.example.translatorkmm.data.source.TranslateEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getLanguages(): List<LanguageEntity>

    suspend fun getLanguage(code: String): LanguageEntity?

    suspend fun getRecentlyUsedSourceLanguage(): LanguageEntity?

    suspend fun getRecentlyUsedTargetLanguage(): LanguageEntity?

    suspend fun getRecentlyUsedSourceLanguages(): List<LanguageEntity>

    suspend fun getRecentlyUsedTargetLanguages(): List<LanguageEntity>

    val translatesInHistory: Flow<List<TranslateEntity>>

    val favoriteTranslates: Flow<List<TranslateEntity>>

    suspend fun updateLanguages(list: List<String>)

    suspend fun updateLanguageUsage(language: Language, direction: LangDirection)

    suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: Language,
        languageTarget: Language
    )

    suspend fun saveAsFavorite(translate: Translate)

    suspend fun removeFromFavorites(translate: Translate)

    suspend fun clearHistory()

    suspend fun clearFavorites()

    suspend fun initFirstSelectedLanguages()

}

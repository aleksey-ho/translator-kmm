package com.example.translator_kmm.usecase

import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.data.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetTranslatesUseCase constructor(
    private val repository: Repository
) {

    val translatesInHistory: Flow<List<Translate>>
        get() = repository.translatesInHistory

    val favoriteTranslates: Flow<List<Translate>>
        get() = repository.favoriteTranslates

    suspend fun addTranslate(
        textSource: String,
        textTranslate: String,
        languageSource: Language,
        languageTarget: Language
    ) {
        withContext(Dispatchers.Default) {
            repository.addTranslate(textSource, textTranslate, languageSource, languageTarget)
        }
    }

    suspend fun saveAsFavorite(translate: Translate) {
        repository.saveAsFavorite(translate)
    }

    suspend fun removeFromFavorites(translate: Translate) {
        return repository.removeFromFavorites(translate)
    }

    suspend fun getTranslate(langSource: Language, langTarget: Language, text: String): Translate {
        return repository.getTranslate(langSource, langTarget, text)
    }

    suspend fun clearHistory() {
        repository.clearHistory()
    }

    suspend fun clearFavorites() {
        repository.clearFavorites()
    }

}
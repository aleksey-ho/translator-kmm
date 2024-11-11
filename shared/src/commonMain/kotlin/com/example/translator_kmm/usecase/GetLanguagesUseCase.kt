package com.example.translator_kmm.usecase

import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.repository.Repository

class GetLanguagesUseCase constructor(
    private val repository: Repository
) {

    suspend fun getLanguages(): List<Language> {
        return repository.getLanguages()
    }

    suspend fun getRecentlyUsedSourceLanguage(): Language? {
        return repository.getRecentlyUsedSourceLanguage()
    }

    suspend fun getRecentlyUsedTargetLanguage(): Language? {
        return repository.getRecentlyUsedTargetLanguage()
    }

    suspend fun getRecentlyUsedSourceLanguages(): List<Language> {
        return repository.getRecentlyUsedSourceLanguages()
    }

    suspend fun getRecentlyUsedTargetLanguages(): List<Language> {
        return repository.getRecentlyUsedTargetLanguages()
    }

    suspend fun updateLanguageUsage(language: Language, direction: LangDirection) {
        return repository.updateLanguageUsage(language, direction)
    }

}
package com.example.translator_kmm.usecase

import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.repository.Repository

class DownloadLanguageModelUseCase constructor(
    private val repository: Repository
) {

    suspend fun downloadLanguageModels() {
        repository.downloadLanguageModels()
    }

    suspend fun downloadLanguageModel(language: Language) {
        repository.downloadLanguageModel(language)
    }

}
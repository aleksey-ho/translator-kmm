package com.example.translator_kmm.data.source

import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate

interface RemoteDataSource {

    fun loadRemoteLanguages(): List<String>

    suspend fun getTranslate(langSource: Language, langTarget: Language, text: String): Translate

    suspend fun downloadLanguageModel(language: Language)

}

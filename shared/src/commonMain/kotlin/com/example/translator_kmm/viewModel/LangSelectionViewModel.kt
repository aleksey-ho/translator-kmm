package com.example.translator_kmm.viewModel

import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.usecase.GetLanguagesUseCase
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LangSelectionViewModel: ViewModel(), KoinComponent {

    private val getLanguagesUseCase: GetLanguagesUseCase by inject()

    private var direction: LangDirection? = null

    fun setDirection(direction: LangDirection) {
        this.direction = direction
    }

    suspend fun getRecentlyUsedLanguages(): List<Language> {
        return if (direction == LangDirection.SOURCE)
            getLanguagesUseCase.getRecentlyUsedSourceLanguages()
        else
            getLanguagesUseCase.getRecentlyUsedTargetLanguages()
    }

    suspend fun getLanguages(): List<Language> {
        return getLanguagesUseCase.getLanguages().sortedBy { language: Language -> language.name }
    }

    fun updateLanguageUsage(language: Language, direction: LangDirection) {
        viewModelScope.launch(Dispatchers.Default) {
            getLanguagesUseCase.updateLanguageUsage(language, direction)
        }
    }

}
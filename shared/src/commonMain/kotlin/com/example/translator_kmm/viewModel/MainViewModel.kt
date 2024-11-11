package com.example.translator_kmm.viewModel

import com.example.translator_kmm.data.repository.SettingsRepository
import com.example.translator_kmm.usecase.DownloadLanguageModelUseCase
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainViewModel : ViewModel(), KoinComponent {

    private val downloadLanguageModelUseCase: DownloadLanguageModelUseCase by inject()
    private val settingsRepository: SettingsRepository by inject()

    fun downloadLanguageModels() {
        viewModelScope.launch {
            try {
                downloadLanguageModelUseCase.downloadLanguageModels()
                settingsRepository.saveBoolean(SettingsRepository.DATA_USAGE_WARNING, false)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun getShowInitDialog(): Boolean {
        return settingsRepository.getBooleanValue(SettingsRepository.DATA_USAGE_WARNING, true)
    }

}


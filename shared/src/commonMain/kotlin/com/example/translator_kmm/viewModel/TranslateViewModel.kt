package com.example.translator_kmm.viewModel

import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.MR
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.usecase.DownloadLanguageModelUseCase
import com.example.translator_kmm.usecase.GetLanguagesUseCase
import com.example.translator_kmm.usecase.GetTranslatesUseCase
import com.example.translator_kmm.utils.DownloadInProgressException
import com.example.translator_kmm.utils.ResourceManager
import dev.icerock.moko.mvvm.flow.CMutableStateFlow
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cMutableStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.seconds

class TranslateViewModel : ViewModel(), KoinComponent {

    private val resourceManager: ResourceManager by inject()
    private val getTranslatesUseCase: GetTranslatesUseCase by inject()
    private val getLanguagesUseCase: GetLanguagesUseCase by inject()
    private val downloadLanguageModelUseCase: DownloadLanguageModelUseCase by inject()

    private val _showErrorDialog: MutableStateFlow<Throwable?> = MutableStateFlow(null)
    val showErrorDialog: CStateFlow<Throwable?>
        get() = _showErrorDialog.cStateFlow()

    private val _languageSource: MutableStateFlow<Language?> = MutableStateFlow(null)
    val languageSource: CStateFlow<Language?>
        get() = _languageSource.cStateFlow()

    private val _languageTarget: MutableStateFlow<Language?> = MutableStateFlow(null)
    val languageTarget: CStateFlow<Language?>
        get() = _languageTarget.cStateFlow()

    private var _textSource = ""

    private val _translate: MutableStateFlow<String> = MutableStateFlow("")
    val translate: CStateFlow<String>
        get() = _translate.cStateFlow()

    private val _internetConnectionError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val internetConnectionError: CStateFlow<Boolean>
        get() = _internetConnectionError.cStateFlow()

    fun loadLanguages() {
        viewModelScope.launch {
            try {
                val recentlyUsedSourceLanguage = getLanguagesUseCase.getRecentlyUsedSourceLanguage()
                _languageSource.value = recentlyUsedSourceLanguage
                val recentlyUsedTargetLanguage = getLanguagesUseCase.getRecentlyUsedTargetLanguage()
                _languageTarget.value = recentlyUsedTargetLanguage
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun setLanguageSource(language: Language) {
        _languageSource.value = language
    }

    fun setLanguageTarget(language: Language) {
        _languageTarget.value = language
    }

    /**
     * Translates text and saves result in database
     */
    fun translateText(text: String?) {
        viewModelScope.launch {
            translateText(text, true)
        }
    }

    fun translateText(text: String?, saveOnCompleted: Boolean) {
        if (text.isNullOrEmpty()) {
            clearTranslate()
            return
        }
        _textSource = text

        viewModelScope.launch {
            try {
                println("translateText viewModelScope.launch getTranslate")
                val translate = getTranslatesUseCase.getTranslate(
                    _languageSource.value ?: return@launch,
                    _languageTarget.value ?: return@launch,
                    _textSource,
                )
                println("translateText finished")
                _internetConnectionError.value = false
                setTranslate(translate.textTarget)
                if (saveOnCompleted) {
                    println("translateText saveTranslate")
                    saveTranslate()
                }
            } catch (error: Throwable) {
                println("translateText error")
                error.printStackTrace()
                // TODO: return
//            if (error is UnknownHostException) {
//                _internetConnectionError.value = true
//                clearTranslate()
//            } else
                if (error is DownloadInProgressException) {
                    println("translateText DownloadInProgressException")
                    _internetConnectionError.value = false
                    _translate.value =
                        resourceManager.getString(MR.strings.download_in_progress_error)
                } else {
                    _internetConnectionError.value = false
                    _showErrorDialog.value = error
                }
            }
        }
    }

    fun swapLanguages() {
        val tempLanguageSource = _languageSource.value
        _languageSource.value = _languageTarget.value
        tempLanguageSource?.let { _languageTarget.value = it }
        viewModelScope.launch {
            try {
                getLanguagesUseCase.updateLanguageUsage(
                    _languageSource.value ?: return@launch,
                    LangDirection.SOURCE
                )
                getLanguagesUseCase.updateLanguageUsage(
                    _languageTarget.value ?: return@launch,
                    LangDirection.TARGET
                )
                translateText(_textSource)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun setSourceText(value: String) {
        _textSource = value
    }

    fun setTranslate(value: String) {
        _translate.value = value
    }

    fun clearSourceText() {
        setSourceText("")
    }

    fun clearTranslate() {
        setTranslate("")
    }

    fun saveTranslate() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.addTranslate(
                    _textSource,
                    _translate.value,
                    _languageSource.value ?: return@launch,
                    _languageTarget.value ?: return@launch,
                )
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    private suspend fun downloadLanguageModel(language: Language) {
        try {
            downloadLanguageModelUseCase.downloadLanguageModel(language)
            translateText(_textSource)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    fun langSelected(language: Language, direction: LangDirection) {
        viewModelScope.launch {
            if (direction == LangDirection.SOURCE)
                setLanguageSource(language)
            else
                setLanguageTarget(language)
            downloadLanguageModel(language)
        }
    }

    fun openTranslate(translate: Translate) {
        setLanguageSource(translate.languageSource)
        setLanguageTarget(translate.languageTarget)
        setSourceText(translate.textSource)
        setTranslate(translate.textTarget)
    }

}
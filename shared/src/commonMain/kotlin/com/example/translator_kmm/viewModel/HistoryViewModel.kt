package com.example.translator_kmm.viewModel

import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.usecase.GetTranslatesUseCase
import dev.icerock.moko.mvvm.flow.CStateFlow
import dev.icerock.moko.mvvm.flow.cStateFlow
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HistoryViewModel : ViewModel(), KoinComponent {

    private val getTranslatesUseCase: GetTranslatesUseCase by inject()

    private val _history = getTranslatesUseCase.translatesInHistory
    val history: CStateFlow<List<Translate>> = _history
//        .flatMapConcat {
//            it
//        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .cStateFlow()

    private val _favorites = getTranslatesUseCase.favoriteTranslates
    val favorites: CStateFlow<List<Translate>> = _favorites
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        .cStateFlow()

    fun saveAsFavorite(translate: Translate) {
        println("222 saveAsFavorite 1")
        viewModelScope.launch {
            try {
                println("222 saveAsFavorite 2")
                getTranslatesUseCase.saveAsFavorite(translate)
                println("222 saveAsFavorite 3")
            } catch (throwable: Throwable) {
                println("22 saveAsFavorite thorw 4")
                throwable.printStackTrace()
            }
        }
    }

    fun removeFromFavorites(translate: Translate) {
        println("3333 removeFromFavorites 1")
        viewModelScope.launch {
            try {
                println("3333 removeFromFavorites 2")
                getTranslatesUseCase.removeFromFavorites(translate)
                println("3333 removeFromFavorites 3")
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                println("333 removeFromFavorites thorw")
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.clearHistory()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    fun clearFavorites() {
        viewModelScope.launch {
            try {
                getTranslatesUseCase.clearFavorites()
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}

package com.example.translator_kmm.android.presentation.history.historyPage

import com.example.translator_kmm.data.model.Translate

interface HistoryListener {

    fun saveAsFavorite(translate: Translate)

    fun removeFromFavorites(translate: Translate)

    fun openTranslate(translate: Translate)

}

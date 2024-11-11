package com.example.translator_kmm.android.presentation.history.historyPage

import androidx.annotation.DrawableRes
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.R

class FavoritesPageFragment : BaseHistoryPageFragment() {

    @DrawableRes
    override val emptyViewImage = R.drawable.ic_favorite_dark
    override val emptyViewText = MR.strings.favorites_empty
    override val dataSource by lazy { viewModel.favorites }

    companion object {
        fun newInstance(): FavoritesPageFragment {
            return FavoritesPageFragment()
        }
    }

}

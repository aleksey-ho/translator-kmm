package com.example.translator_kmm.android.presentation.history.historyPage

import androidx.annotation.DrawableRes
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.R

class HistoryPageFragment : BaseHistoryPageFragment() {

    @DrawableRes
    override val emptyViewImage = R.drawable.ic_access_time_black_24dp
    override val emptyViewText = MR.strings.history_empty
    override val dataSource by lazy { viewModel.history }

    companion object {
        fun newInstance(): HistoryPageFragment {
            return HistoryPageFragment()
        }
    }

}

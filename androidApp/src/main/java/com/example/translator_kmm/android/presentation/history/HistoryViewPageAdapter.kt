package com.example.translator_kmm.android.presentation.history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.translator_kmm.android.presentation.history.historyPage.FavoritesPageFragment
import com.example.translator_kmm.android.presentation.history.historyPage.HistoryPageFragment

class HistoryViewPageAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var historyPageFragment = HistoryPageFragment.newInstance()
    private var favoritesPageFragment = FavoritesPageFragment.newInstance()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            historyPageFragment
        } else {
            favoritesPageFragment
        }
    }
}

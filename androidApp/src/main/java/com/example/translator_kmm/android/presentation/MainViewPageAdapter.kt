package com.example.translator_kmm.android.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.translator_kmm.android.presentation.history.HistoryFragment
import com.example.translator_kmm.android.presentation.settings.SettingsFragment
import com.example.translator_kmm.android.presentation.translate.TranslateFragment

class MainViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    internal var translateFragment = TranslateFragment.newInstance()
    internal var historyFragment = HistoryFragment.newInstance()
    internal var settingsFragment = SettingsFragment.newInstance()

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> translateFragment
            1 -> historyFragment
            else -> settingsFragment
        }
    }

}
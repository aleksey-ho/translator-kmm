package com.example.translator_kmm.android.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.databinding.FragmentHistoryBinding
import com.example.translator_kmm.android.presentation.dialog.Dialog
import com.example.translator_kmm.viewModel.HistoryViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewbinding.MvvmFragment
import dev.icerock.moko.resources.desc.desc

class HistoryFragment : MvvmFragment<FragmentHistoryBinding, HistoryViewModel>() {

    override val viewModelClass: Class<HistoryViewModel> = HistoryViewModel::class.java
    private lateinit var adapter: HistoryViewPageAdapter

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(layoutInflater, container, false)
    }

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            HistoryViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HistoryViewPageAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = (if (position == 0)
                MR.strings.view_pager_history
            else
                MR.strings.view_pager_favorites)
                .desc().toString(requireContext())
        }.attach()

        binding.buttonClearHistory.setOnClickListener { clearHistory() }
    }

    private fun clearHistory() {
        val clearHistory = binding.viewPager.currentItem == 0
        Dialog().showClearHistoryDialog(
            requireActivity(),
            clearHistory,
            onButtonClick = {
                if (clearHistory) {
                    viewModel.clearHistory()
                } else {
                    viewModel.clearFavorites()
                }
            }
        )
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

}

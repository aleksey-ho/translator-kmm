package com.example.translator_kmm.android.presentation.history.historyPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translator_kmm.android.databinding.FragmentHistoryPageBinding
import com.example.translator_kmm.android.presentation.MainActivity
import com.example.translator_kmm.android.presentation.MainRouter
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.viewModel.HistoryViewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewbinding.MvvmFragment
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.fragmentScope
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent.getKoin

abstract class BaseHistoryPageFragment : MvvmFragment<FragmentHistoryPageBinding, HistoryViewModel>(),
    HistoryListener,
    AndroidScopeComponent {

    override val scope : Scope by fragmentScope()
    override val viewModelClass: Class<HistoryViewModel> = HistoryViewModel::class.java
    private lateinit var mainRouter: MainRouter
    lateinit var historyRecyclerAdapter: HistoryRecyclerAdapter

    abstract val emptyViewImage: Int
    abstract val emptyViewText: StringResource
    abstract val dataSource: StateFlow<List<Translate>>

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHistoryPageBinding {
        return FragmentHistoryPageBinding.inflate(layoutInflater, container, false)
    }

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            HistoryViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ourSession = getKoin().getOrCreateScope(MainActivity.SCOPE_ID, named(MainActivity.SCOPE_NAME))
        scope.linkTo(ourSession)
        mainRouter = get()
        initAdapter()

        binding.imageView.setBackgroundResource(emptyViewImage)
        binding.textView.text = emptyViewText.desc().toString(requireContext())
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                dataSource.collect { items ->
                    historyRecyclerAdapter.items = items
                    binding.emptyView.visibility = if (items.isNotEmpty()) View.GONE else View.VISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        historyRecyclerAdapter = HistoryRecyclerAdapter(requireContext(), this)
        binding.listViewHistory.adapter = historyRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.listViewHistory.layoutManager = linearLayoutManager
    }

    override fun saveAsFavorite(translate: Translate) {
        viewModel.saveAsFavorite(translate)
    }

    override fun removeFromFavorites(translate: Translate) {
        viewModel.removeFromFavorites(translate)
    }

    override fun openTranslate(translate: Translate) {
        try {
            mainRouter.openTranslate(translate)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

}

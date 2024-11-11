package com.example.translator_kmm.android.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.translator_kmm.android.R
import com.example.translator_kmm.android.databinding.ActivityMainBinding
import com.example.translator_kmm.android.presentation.dialog.Dialog
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.viewModel.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewbinding.MvvmActivity
import org.koin.android.ext.android.get
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.java.KoinJavaComponent

class MainActivity: MvvmActivity<ActivityMainBinding, MainViewModel>(), MainRouter.Delegate,
    AndroidScopeComponent {

    override val scope : Scope by activityScope()
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    private lateinit var mainRouter: MainRouter
    private lateinit var adapter: MainViewPageAdapter

    override fun viewBindingInflate(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            MainViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ourSession = KoinJavaComponent.getKoin().getOrCreateScope(SCOPE_ID, named(SCOPE_NAME))
        scope.linkTo(ourSession)
        mainRouter = get()
        mainRouter.delegate = this

        supportActionBar?.hide()
        adapter = MainViewPageAdapter(this)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(
                when (position) {
                    0 -> R.drawable.ic_translate
                    1 -> R.drawable.ic_history
                    else -> R.drawable.ic_settings
                }
            )
        }.attach()

        binding.viewPager.isUserInputEnabled = false

        if (viewModel.getShowInitDialog()) {
            Dialog().showInitDialog(
                this,
                onButtonClick = {
                    viewModel.downloadLanguageModels()
                }
            )
        }

    }

    override fun openTranslate(translate: Translate) {
        binding.viewPager.setCurrentItem(0, true)
        adapter.translateFragment.openTranslate(translate)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    adapter.translateFragment.saveTranslate()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    companion object {
        const val SCOPE_ID = "MainActivityScopeId"
        const val SCOPE_NAME = "MainActivityScopeName"
    }

}

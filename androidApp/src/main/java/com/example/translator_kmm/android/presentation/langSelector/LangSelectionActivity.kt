package com.example.translator_kmm.android.presentation.langSelector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.databinding.ActivityLangSelectionBinding
import com.example.translator_kmm.android.presentation.translate.TranslateFragment.Companion.EXTRA_DIRECTION
import com.example.translator_kmm.android.presentation.translate.TranslateFragment.Companion.EXTRA_ITEM
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.viewModel.LangSelectionViewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewbinding.MvvmActivity
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LangSelectionActivity : MvvmActivity<ActivityLangSelectionBinding, LangSelectionViewModel>() {

    override val viewModelClass: Class<LangSelectionViewModel> = LangSelectionViewModel::class.java
    private lateinit var direction: LangDirection

    override fun viewBindingInflate(): ActivityLangSelectionBinding {
        return ActivityLangSelectionBinding.inflate(layoutInflater)
    }

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            LangSelectionViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        direction = LangDirection.values()[intent.getIntExtra(DIRECTION, 0)]
        viewModel.setDirection(direction)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = (if (direction == LangDirection.SOURCE)
            MR.strings.language_source
        else
            MR.strings.language_target)
            .desc().toString(this)

        val adapter = LangSelectionAdapter(this)
        lifecycleScope.launch {
            try {
                // add recently used languages to list (if any)
                val recentlyUsedLanguages = viewModel.getRecentlyUsedLanguages()
                if (recentlyUsedLanguages.isNotEmpty()) {
                    adapter.addSectionHeaderItem(
                        MR.strings.recently_used.desc().toString(this@LangSelectionActivity)
                    )
                    adapter.addItems(recentlyUsedLanguages)
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }

            try {
                // add all the languages to list
                val languages = viewModel.getLanguages()
                if (languages.isNotEmpty()) {
                    adapter.addSectionHeaderItem(
                        MR.strings.all_languages.desc().toString(this@LangSelectionActivity)
                    )
                    adapter.addItems(languages)
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }

            binding.listViewLanguages.adapter = adapter
            binding.listViewLanguages.setOnItemClickListener { parent, _, position, _ ->
                val item = parent.adapter.getItem(position)
                if (item is Language) {
                    try {
                        viewModel.updateLanguageUsage(item, direction)
                        val data = Intent()
                        data.putExtra(EXTRA_ITEM, Json.encodeToString(item))
                        data.putExtra(EXTRA_DIRECTION, direction.value)
                        setResult(RESULT_OK, data)
                        finish()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val DIRECTION = "DIRECTION"

        fun newIntent(context: Context, direction: LangDirection): Intent {
            val intent = Intent(context, LangSelectionActivity::class.java)
            intent.putExtra(DIRECTION, direction.value)
            return intent
        }
    }

}

package com.example.translator_kmm.android.presentation.translate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.translator_kmm.LangDirection
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.databinding.FragmentTranslateBinding
import com.example.translator_kmm.android.databinding.TextInputPanelBinding
import com.example.translator_kmm.android.presentation.dialog.Dialog
import com.example.translator_kmm.android.presentation.langSelector.LangSelectionActivity
import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.data.model.Translate
import com.example.translator_kmm.viewModel.TranslateViewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewbinding.MvvmFragment
import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TranslateFragment : MvvmFragment<FragmentTranslateBinding, TranslateViewModel>() {

    override val viewModelClass: Class<TranslateViewModel> = TranslateViewModel::class.java
    private lateinit var textInputPanelBinding: TextInputPanelBinding

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTranslateBinding {
        return FragmentTranslateBinding.inflate(layoutInflater, container, false)
    }

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            TranslateViewModel()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        textInputPanelBinding = binding.textInputPanel
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadLanguages()
        savedInstanceState?.getString(TEXT_SOURCE)?.let { viewModel.setSourceText(it) }
        savedInstanceState?.getString(TEXT_TRANSLATE)?.let { viewModel.setTranslate(it) }
        textInputPanelBinding.editTextToTranslate.hint =
            MR.strings.hint_enter_text.desc().toString(requireContext())
        binding.connectionErrorTextView.text =
            MR.strings.error_connection.desc().toString(requireContext())
        binding.connectionErrorDescTextView.text =
            MR.strings.error_connection_desc.desc().toString(requireContext())
        binding.buttonTryAgain.text =
            MR.strings.error_button_try_again.desc().toString(requireContext())
        binding.copyrightTextView.text = MR.strings.copyright.desc().toString(requireContext())

        binding.textViewLangSource.setOnClickListener {
            startActivityForResult(
                LangSelectionActivity.newIntent(
                    requireContext(),
                    LangDirection.SOURCE
                ), REQUEST_CODE
            )
        }
        binding.textViewLangTarget.setOnClickListener {
            startActivityForResult(
                LangSelectionActivity.newIntent(
                    requireContext(),
                    LangDirection.TARGET
                ), REQUEST_CODE
            )
        }
        binding.buttonTryAgain.setOnClickListener {
            viewModel.translateText(textInputPanelBinding.editTextToTranslate.text.toString())
        }
        textInputPanelBinding.buttonClear.setOnClickListener {
            viewModel.clearSourceText()
            viewModel.clearTranslate()
            textInputPanelBinding.editTextToTranslate.setText("")
            textInputPanelBinding.editTextToTranslate.requestFocus()
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                textInputPanelBinding.editTextToTranslate,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
        textInputPanelBinding.buttonVoiceInput.setOnClickListener {
            Toast.makeText(
                context,
                MR.strings.not_implemented.desc().toString(requireContext()),
                Toast.LENGTH_SHORT
            ).show()
        }
        textInputPanelBinding.buttonPlayback.setOnClickListener {
            Toast.makeText(
                context,
                MR.strings.not_implemented.desc().toString(requireContext()),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonSwap.setOnClickListener { viewModel.swapLanguages() }

        textInputPanelBinding.editTextToTranslate.setRawInputType(InputType.TYPE_CLASS_TEXT)
        textInputPanelBinding.editTextToTranslate.setOnEditorActionListener { v, actionId, _ ->
            var handled = false
            // when user stops typing and presses Enter
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (v.text.isNotEmpty())
                    viewModel.translateText(textInputPanelBinding.editTextToTranslate.text.toString())
                else {
                    viewModel.clearTranslate()
                    viewModel.clearSourceText()
                }
                handled = true
                v.clearFocus()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            handled
        }

        // when user is typing - translate in real-time
        textInputPanelBinding.editTextToTranslate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.translateText(
                    textInputPanelBinding.editTextToTranslate.text.toString(),
                    false
                )
            }
        })

        binding.textViewTranslate.movementMethod = ScrollingMovementMethod()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.showErrorDialog.collect { showErrorDialog ->
                        showErrorDialog?.let {
                            showErrorDialog(it)
                        }
                    }
                }
                launch {
                    viewModel.translate.collect {
                        binding.textViewTranslate.text = it
                    }
                }
                launch {
                    viewModel.languageSource.collect {
                        binding.textViewLangSource.text = it?.name
                    }
                }
                launch {
                    viewModel.languageTarget.collect {
                        binding.textViewLangTarget.text = it?.name
                    }
                }
                launch {
                    viewModel.internetConnectionError.collect {
                        binding.internetConnectionError.visibility = if (it) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }
                }
            }
        }
    }

    fun saveTranslate() {
        viewModel.saveTranslate()
    }

    fun openTranslate(translate: Translate) {
        viewModel.openTranslate(translate)
        textInputPanelBinding.editTextToTranslate.setText(translate.textSource)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(
            TEXT_SOURCE,
            textInputPanelBinding.editTextToTranslate.text.toString()
        )
        savedInstanceState.putString(TEXT_TRANSLATE, binding.textViewTranslate.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun showErrorDialog(error: Throwable) {
        Dialog().showErrorDialog(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val languageString = data?.getStringExtra(EXTRA_ITEM) ?: return
            val language = Json.decodeFromString<Language>(languageString)
            val direction = data.getIntExtra(EXTRA_DIRECTION, 0)
            viewModel.langSelected(language, LangDirection.values()[direction])
        }
    }

    companion object {
        const val TEXT_SOURCE = "TEXT_SOURCE"
        const val TEXT_TRANSLATE = "TEXT_TRANSLATE"
        const val EXTRA_ITEM = "EXTRA_ITEM"
        const val EXTRA_DIRECTION = "EXTRA_DIRECTION"
        const val REQUEST_CODE = 0

        fun newInstance(): TranslateFragment {
            return TranslateFragment()
        }
    }

}

package com.example.translator_kmm.android.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.translator_kmm.MR
import com.example.translator_kmm.android.databinding.FragmentSettingsBinding
import com.example.translator_kmm.viewModel.SettingsViewModel
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.viewbinding.MvvmFragment
import dev.icerock.moko.resources.desc.desc

class SettingsFragment : MvvmFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override val viewModelClass: Class<SettingsViewModel> = SettingsViewModel::class.java

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(layoutInflater, container, false)
    }

    override fun viewModelFactory(): ViewModelProvider.Factory {
        return createViewModelFactory {
            SettingsViewModel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = MR.strings.tab_settings.desc().toString(requireContext())
        binding.switch1.text = MR.strings.switch1.desc().toString(requireContext())
        binding.switch2.text = MR.strings.switch2.desc().toString(requireContext())
        binding.switch3.text = MR.strings.switch3.desc().toString(requireContext())
    }

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

}

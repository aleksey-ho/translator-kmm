package com.example.translator_kmm.data.source

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class TranslateMLKitManagerProxy: KoinComponent {

    private val translateMLKitManager: TranslateMLKitManager by inject()

    fun setDelegate(nativeDelegate: TranslateMLKitManager) {
        (translateMLKitManager as? TranslateMLKitManagerNative)?.nativeDelegate = nativeDelegate
    }

}
package com.example.translator_kmm.utils

import com.example.translator_kmm.data.source.TranslateMLKitManager

lateinit var getLanguageFromCode: (code: String) -> String

actual class LocaleManager {

//    override var nativeDelegate: TranslateMLKitManager? = null

    actual fun getLocaleNameFromCode(code: String): String {
        return getLanguageFromCode(code)
    }

}
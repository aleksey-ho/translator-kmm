package com.example.translator_kmm.utils

import java.util.*


actual class LocaleManager {

    actual fun getLocaleNameFromCode(code: String): String {
        return Locale(code).displayName.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

}
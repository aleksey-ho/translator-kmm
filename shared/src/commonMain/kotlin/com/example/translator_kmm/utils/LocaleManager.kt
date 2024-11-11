package com.example.translator_kmm.utils

expect class LocaleManager() {
    fun getLocaleNameFromCode(code: String): String
}
package com.example.translator_kmm.data.mapper

import com.example.translator_kmm.data.model.Language
import com.example.translator_kmm.utils.LocaleManager
import com.example.translatorkmm.data.source.LanguageEntity


fun LanguageEntity.plain() = Language(
    code = code,
    name = LocaleManager().getLocaleNameFromCode(code),
    sourceLastUseDate = sourceLastUseDate,
    targetLastUseDate = targetLastUseDate,
    usageCounter = usageCounter,
)

fun List<LanguageEntity>.plainLanguageEntityList(): List<Language> {
    return this.map { it.plain() }
}

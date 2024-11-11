package com.example.translator_kmm.di

import com.example.translator_kmm.usecase.DownloadLanguageModelUseCase
import com.example.translator_kmm.usecase.GetLanguagesUseCase
import com.example.translator_kmm.usecase.GetTranslatesUseCase
import org.koin.dsl.module

val domainModule = module {

    factory {
        DownloadLanguageModelUseCase(repository = get())
    }

    factory {
        GetLanguagesUseCase(repository = get())
    }

    factory {
        GetTranslatesUseCase(repository = get())
    }

}


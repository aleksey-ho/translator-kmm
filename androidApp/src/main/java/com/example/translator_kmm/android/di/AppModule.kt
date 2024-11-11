package com.example.translator_kmm.android.di

import com.example.translator_kmm.android.presentation.MainActivity
import com.example.translator_kmm.android.presentation.MainRouter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    scope(named(MainActivity.SCOPE_NAME)) {
        scoped<MainRouter> {
            MainRouter()
        }
    }

}

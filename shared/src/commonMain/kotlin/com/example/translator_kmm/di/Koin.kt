package com.example.translator_kmm.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(dataModule, platformDataModule(), domainModule)
    }

// called by iOS
fun initKoin() = initKoin {}

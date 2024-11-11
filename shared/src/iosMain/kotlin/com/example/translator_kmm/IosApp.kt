package com.example.translator_kmm

import com.example.translator_kmm.data.repository.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class IosApp: KoinComponent {
    val repository: Repository by inject()

    suspend fun init() {
        repository.loadLanguages()
    }

}
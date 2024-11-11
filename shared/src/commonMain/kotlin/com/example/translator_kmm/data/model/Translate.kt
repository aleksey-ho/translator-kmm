package com.example.translator_kmm.data.model

import kotlinx.datetime.Instant
import kotlin.random.Random

data class Translate(
    var id: Long = -1,
    var languageSource: Language,
    var languageTarget: Language,
    var date: Instant,
    var textSource: String,
    var textTarget: String,
    var savedInHistory: Boolean = false,
    var savedInFavorites: Boolean = false
) {

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return Random.nextInt()
    }

}
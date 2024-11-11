package com.example.translator_kmm.data.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    var code: String,
    var name: String,
    var sourceLastUseDate: Instant? = null,
    var targetLastUseDate: Instant? = null,
    var usageCounter: Int
)

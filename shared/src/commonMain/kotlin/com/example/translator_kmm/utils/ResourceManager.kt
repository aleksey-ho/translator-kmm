package com.example.translator_kmm.utils

import dev.icerock.moko.resources.StringResource

expect class ResourceManager {
    fun getString(stringResource: StringResource): String

}
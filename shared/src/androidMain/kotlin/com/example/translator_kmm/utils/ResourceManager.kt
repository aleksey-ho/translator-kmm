package com.example.translator_kmm.utils

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc


actual class ResourceManager(private val context: Context) {

    actual fun getString(stringResource: StringResource): String {
        return stringResource.desc().toString(context)
    }

}
package com.example.translator_kmm.utils

import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc

actual class ResourceManager {

    actual fun getString(stringResource: StringResource): String {
        return stringResource.desc().localized()
    }

}
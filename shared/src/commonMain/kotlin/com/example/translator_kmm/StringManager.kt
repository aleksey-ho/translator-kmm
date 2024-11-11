package com.example.translator_kmm

import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc

class StringManager {

    fun getAppName(): StringDesc {
        return StringDesc.Resource(MR.strings.app_name)
    }

}
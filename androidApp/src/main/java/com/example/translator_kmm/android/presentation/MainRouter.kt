package com.example.translator_kmm.android.presentation

import com.example.translator_kmm.data.model.Translate

class MainRouter {

    var delegate: Delegate? = null

    fun openTranslate(translate: Translate) {
        delegate?.openTranslate(translate)
    }

    interface Delegate {
        fun openTranslate(translate: Translate)
    }

}

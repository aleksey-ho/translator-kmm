package com.example.translator_kmm.android

import android.app.Application
import com.example.translator_kmm.android.di.appModule
import com.example.translator_kmm.data.repository.Repository
import com.example.translator_kmm.di.initKoin
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

open class App : Application() {

    private val scope = MainScope()
    private val repository: Repository by inject()

    override fun onCreate() {
        app = this
        super.onCreate()
        initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
//            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(appModule)
        }
        loadLanguages()
    }

    private fun loadLanguages() {
        runBlocking {
            try {
                repository.loadLanguages()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        scope.cancel()
    }

    companion object {
        lateinit var app: App
    }

}

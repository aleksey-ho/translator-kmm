package com.example.translator_kmm.data.source

import android.util.LruCache
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

actual class TranslateMLKitManagerImpl : TranslateMLKitManager {

    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()
    private var languageModelIsDownloading = false

    actual override fun isLanguageModelDownloading(): Boolean {
        return languageModelIsDownloading
    }

    private val translators =
        object : LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
            override fun create(options: TranslatorOptions): Translator {
                return Translation.getClient(options)
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: TranslatorOptions,
                oldValue: Translator,
                newValue: Translator?
            ) {
                oldValue.close()
            }
        }

    actual override suspend fun getTranslate(
        langSource: String,
        langTarget: String,
        text: String
    ): String {
        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(langSource)
                .setTargetLanguage(langTarget)
                .build()

        val translate = withContext(Dispatchers.IO) {
            translators[options].downloadModelIfNeeded()
                .continueWithTask { task ->
                    if (task.isSuccessful) {
                        return@continueWithTask translators[options].translate(text)
                    } else {
                        Tasks.forException(task.exception ?: Exception("Unknown error"))
                    }
                }.await()
        }

        return translate
    }

    actual override fun loadRemoteLanguages(): List<String> {
        return TranslateLanguage.getAllLanguages()
    }

    // Starts downloading a remote model for local translation.
    actual override suspend fun downloadLanguageModel(language: String) {
        languageModelIsDownloading = true
        val languageCode = TranslateLanguage.fromLanguageTag(language) ?: return
        val model = getModel(languageCode)
        withContext(Dispatchers.IO) {
            modelManager.download(model, DownloadConditions.Builder().build()).await()
        }
        languageModelIsDownloading = false
    }

    private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }

    companion object {
        private const val NUM_TRANSLATORS = 3
    }

}
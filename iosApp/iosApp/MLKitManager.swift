//
//  MLKitManager.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared
import MLKit

class MLKitManager : TranslateMLKitManager {
    
    var translator: Translator!
    lazy var allLanguages = TranslateLanguage.allLanguages()
    let locale = Locale.current
    
    private var languageModelIsDownloading: Bool = false
    
    func isLanguageModelDownloading() -> Bool {
        return languageModelIsDownloading
    }
    
    static func doInit() {
        TranslateMLKitManagerProxy().setDelegate(nativeDelegate: MLKitManager())
    }

    func downloadLanguageModel(language: String, completionHandler_ completionHandler: @escaping ((any Error)?) -> Void) {
        languageModelIsDownloading = true
        let model = self.model(forLanguage: language)
        let modelManager = ModelManager.modelManager()
        
        let conditions = ModelDownloadConditions(
            allowsCellularAccess: true,
            allowsBackgroundDownloading: true
        )
        modelManager.download(model, conditions: conditions)
        languageModelIsDownloading = false
    }
    
    func getTranslate(langSource: String, langTarget: String, text: String, completionHandler_ completionHandler: @escaping (String?, Error?) -> Void) {
        guard let inputLanguage = getLanguage(fromLanguageTag: langSource),
              let outputLanguage = getLanguage(fromLanguageTag: langTarget) else {
            completionHandler(nil, nil)
            return
        }
        let options = TranslatorOptions(sourceLanguage: inputLanguage, targetLanguage: outputLanguage)
        translator = Translator.translator(options: options)
        
        let translatorForDownloading = self.translator!
        
        translatorForDownloading.downloadModelIfNeeded { error in
            guard error == nil else {
                completionHandler(nil, error)
                return
            }
            if translatorForDownloading == self.translator {
                translatorForDownloading.translate(text) { result, error in
                    guard error == nil else {
                        completionHandler(nil, error)
                        return
                    }
                    if translatorForDownloading == self.translator {
                        completionHandler(result, nil)
                    }
                }
            }
        }
    }
    
    func loadRemoteLanguages() -> [String] {
        return TranslateLanguage.allLanguages().map { language in
            language.rawValue
        }
    }

    func model(forLanguage: String) -> TranslateRemoteModel {
        let language = getLanguage(fromLanguageTag: forLanguage)
        return TranslateRemoteModel.translateRemoteModel(language: language!)
    }
    
    func getLanguage(fromLanguageTag: String) -> TranslateLanguage? {
        return TranslateLanguage.allLanguages().first { $0.rawValue == fromLanguageTag }
    }
    
}

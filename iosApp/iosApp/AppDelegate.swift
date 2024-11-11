//
//  AppDelegate.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import shared
import UIKit

@UIApplicationMain
class AppDelegate: NSObject, UIApplicationDelegate {
    
    var window: UIWindow?
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        KoinKt.doInitKoin()

        // initializing Google MLKit
        MLKitManager.doInit()
        
        IosApp().doInit { _ in }
        LocaleManagerKt.getLanguageFromCode = { code in
            return Locale.current.localizedString(forLanguageCode: code)?.capitalized ?? code
        }
        return true
    }
    
}

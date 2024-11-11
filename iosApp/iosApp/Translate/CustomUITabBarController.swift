//
//  CustomUITabBarController.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 14.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import shared

protocol OpenTranslateDelegate: AnyObject {
    func open(translate: Translate)
}

class CustomUITabBarController: UITabBarController {
    
    private var mainViewModel: MainViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        mainViewModel = MainViewModel()
        mainViewModel.downloadLanguageModels()
        
        tabBar.items![0].title = MR.strings().tab_translate.localized()
        tabBar.items![1].title = MR.strings().tab_history.localized()
        tabBar.items![2].title = MR.strings().tab_settings.localized()
        (viewControllers?[1] as? HistoryVC)?.delegate = self
    }
    
}

extension CustomUITabBarController: OpenTranslateDelegate {
    
    func open(translate: Translate) {
        selectedIndex = 0
        (viewControllers?[0] as? TranslateVC)?.open(translate: translate)
    }
    
}

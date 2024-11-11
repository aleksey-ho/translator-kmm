//
//  SettingsVC.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import shared

class SettingsVC: UIViewController {
    
    @IBOutlet weak var label1: UILabel!
    @IBOutlet weak var label2: UILabel!
    @IBOutlet weak var label3: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .darkContent
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let statusBarHeight: CGFloat = UIApplication.shared.statusBarFrame.size.height + 10
        let statusBarView = UIView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: statusBarHeight))
        statusBarView.backgroundColor = UIColor.colorPrimary
        view.addSubview(statusBarView)
        
        titleLabel.backgroundColor = UIColor.colorPrimary
        titleLabel.text = MR.strings().tab_settings.localized()
        
        label1.text = MR.strings().switch1.localized()
        label2.text = MR.strings().switch2.localized()
        label3.text = MR.strings().switch3.localized()
        
    }
    
}

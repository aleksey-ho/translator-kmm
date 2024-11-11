//
//  SimpleViewController.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import shared

class TranslateVC: UIViewController {
    
    private var viewModel: TranslateViewModel!
    
    @IBOutlet weak var languageSelectorView: UIView!
    @IBOutlet weak var sourceLanguageUIButton: UIButton!
    @IBOutlet weak var targetLanguageUIButton: UIButton!
    @IBOutlet weak var inputWidget: InputWidget!
    @IBOutlet weak var outputTextView: UITextView!
    @IBOutlet weak var copyrightLabel: UILabel!
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .darkContent
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let statusBarHeight: CGFloat = UIApplication.shared.statusBarFrame.size.height + 10
        let statusbarView = UIView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: statusBarHeight))
        statusbarView.backgroundColor = UIColor.colorPrimary
        view.addSubview(statusbarView)
        
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(self.dismissKeyboard (_:)))
        self.view.addGestureRecognizer(tapGesture)

        inputWidget.delegate = self
        
        viewModel = TranslateViewModel()
        
        //        translateMLKitManager.languageModelIsDownloading
        
        viewModel.translate.subscribe { value in
            self.outputTextView.text = value as String?
        }
        
        self.viewModel.loadLanguages()
        
        self.viewModel.languageSource.subscribe { language in
            self.sourceLanguageUIButton.setAttributedTitle(NSAttributedString(string: language?.name ?? "", attributes: self.targetLanguageUIButton.titleLabel?.attributedText!.attributes(at: 0, effectiveRange: nil)), for: .normal)
        }
        
        self.viewModel.languageTarget.subscribe { language in
            self.targetLanguageUIButton.setAttributedTitle(NSAttributedString(string: language?.name ?? "", attributes: self.targetLanguageUIButton.titleLabel?.attributedText!.attributes(at: 0, effectiveRange: nil)), for: .normal)
        }
        
        languageSelectorView.backgroundColor = UIColor.colorPrimary
        copyrightLabel.text = MR.strings().copyright.localized()
    }
    
    @objc func dismissKeyboard (_ sender: UITapGestureRecognizer) {
        inputWidget.textView.resignFirstResponder()
        self.translate()
    }
    
    @IBAction func sourceLanguageTapped(_ sender: Any) {
        openLanguageSelectorWith(direction: .source)
    }
    
    @IBAction func targetLanguageTapped(_ sender: Any) {
        openLanguageSelectorWith(direction: .target)
    }
    
    private func openLanguageSelectorWith(direction: LangDirection) {
        let languageSelectorVC = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "LanguageSelectorVC") as! LanguageSelectorVC
        languageSelectorVC.langDirection = direction
        languageSelectorVC.modalPresentationStyle = .pageSheet
        languageSelectorVC.delegate = self
        
        self.present(languageSelectorVC, animated: true)
    }
    
    @IBAction func swapButtonTapped(_ sender: Any) {
        viewModel.swapLanguages()
    }
    
    func open(translate: Translate) {
        viewModel.openTranslate(translate: translate)
        inputWidget.textView.text = translate.textSource
        inputWidget.placeholderLabel.text = ""
        outputTextView.text = translate.textTarget
    }
    
    func translate() {
        self.viewModel.translateText(text: inputWidget.textView.text.trimmingCharacters(in: .whitespacesAndNewlines))
    }
    
}

extension TranslateVC {
    
    override func didMove(toParent parent: UIViewController?) {
        if (parent == nil) {
            viewModel.onCleared()
        }
    }
    
}

extension TranslateVC: InputWidgetDelegate {
    
    func textCleared() {
        outputTextView.text = ""
    }
    
    func textChanged(_ text: String) {
        self.translate()
    }
    
}

extension TranslateVC: LanguageSelectorDelegate {
    
    func languageSelected(language: Language, direction: LangDirection) {
        viewModel.langSelected(language: language, direction: direction)
        self.translate()
    }
    
}

//
//  SimpleViewController.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 10.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import shared

protocol LanguageSelectorDelegate {
    func languageSelected(language: Language, direction: LangDirection)
}

class LanguageSelectorVC: UIViewController {
    
    var langDirection: LangDirection = .source
    var delegate: LanguageSelectorDelegate?
    
    private var languages: [Language] = []
    private var recentlyUsedLanguages: [Language] = []
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .darkContent
    }
    
    private var viewModel: LangSelectionViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        viewModel = LangSelectionViewModel()
        viewModel.setDirection(direction: langDirection)
        viewModel.getLanguages(completionHandler: { languages, error in
            if let languages = languages {
                self.languages = languages
            }
        })
        viewModel.getRecentlyUsedLanguages(completionHandler: { languages, error in
            if let languages = languages {
                self.recentlyUsedLanguages = languages
            }
        })
    }
    
}

extension LanguageSelectorVC {
    
    override func didMove(toParent parent: UIViewController?) {
        if (parent == nil) {
            viewModel.onCleared()
        }
    }
    
}

extension LanguageSelectorVC: UITableViewDataSource, UITableViewDelegate {
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return recentlyUsedLanguages.count > 0 ? 2 : 1
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let cell = tableView.dequeueReusableCell(withIdentifier: "languageSelectorHeaderCell") as! LanguageSelectorHeaderCell
        if recentlyUsedLanguages.count > 0 && section == 0 {
            cell.headerLabel.text = MR.strings().recently_used.localized()
        } else {
            cell.headerLabel.text = MR.strings().all_languages.localized()
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return recentlyUsedLanguages.count > 0 && section == 0 ? recentlyUsedLanguages.count : languages.count
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
            return 62
        }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "languageSelectorCell", for: indexPath) as! LanguageSelectorCell
        if recentlyUsedLanguages.count > 0 && indexPath.section == 0 {
            cell.languageLabel.text = recentlyUsedLanguages[indexPath.row].name
            }
        else {
            cell.languageLabel.text = languages[indexPath.row].name
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let language = if recentlyUsedLanguages.count > 0 && indexPath.section == 0 {
            recentlyUsedLanguages[indexPath.row]
        } else {
            languages[indexPath.row]
        }
        viewModel.updateLanguageUsage(language: language, direction: langDirection)
        delegate?.languageSelected(language: language, direction: langDirection)
        self.dismiss(animated: true)
    }
}

class LanguageSelectorCell: UITableViewCell {
    @IBOutlet weak var languageLabel: UILabel!
}

class LanguageSelectorHeaderCell: UITableViewCell {
    @IBOutlet weak var headerLabel: UILabel!
}

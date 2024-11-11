//
//  HistoryPageVC.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

class HistoryPageVC: UIViewController {
    
    var viewModel: HistoryViewModel!
    var dataSource: [Translate] = []
    weak var delegate: OpenTranslateDelegate?
    var pageIndex = 0
    var screenWidth: CGFloat!
    
    @IBOutlet weak var collectionView: UICollectionView!
    
    static func getInstance(index: Int, viewModel: HistoryViewModel, delegate: OpenTranslateDelegate?) -> HistoryPageVC {
        let vc = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(identifier: "BaseHistoryVC") as! HistoryPageVC
        vc.viewModel = viewModel
        vc.pageIndex = index
        vc.delegate = delegate
        return vc
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        screenWidth = view.frame.width
        
        let source = if pageIndex == 0 {
            viewModel.history
        } else  {
            viewModel.favorites
        }
        source.subscribe { value in
            self.dataSource = value as? [Translate] ?? self.dataSource
            self.collectionView.reloadData()
        }
    }
    
}

extension HistoryPageVC: UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.dataSource.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "HistoryViewCell", for: indexPath as IndexPath) as! HistoryViewCell
        
        let translate = self.dataSource[indexPath.row]
        cell.textSource.text = translate.textSource
        cell.textTarget.text = translate.textTarget
        cell.sourceLanguageCodeUILabel.text = translate.languageSource.code.uppercased()
        cell.targetLanguageCodeUILabel.text = translate.languageTarget.code.uppercased()
        cell.bookmarkUIButton.tag = indexPath.row
        cell.bookmarkUIButton.addTarget(self, action: #selector(bookmarkButtonTapped), for: .touchUpInside)
        
        if translate.savedInFavorites {
            cell.bookmarkUIButton.tintColor = UIColor.colorPrimary
        } else {
            cell.bookmarkUIButton.tintColor = .lightGray
        }
        
        return cell
    }
    
    @objc func bookmarkButtonTapped(sender: UIButton) {
        let translate = dataSource[sender.tag]
        if translate.savedInFavorites {
            viewModel.removeFromFavorites(translate: translate)
            dataSource[sender.tag].savedInFavorites = false
        } else {
            viewModel.saveAsFavorite(translate: translate)
            dataSource[sender.tag].savedInFavorites = true
        }
    }
    
}

extension HistoryPageVC: UICollectionViewDelegate {

    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        // handle tap events
        print("You selected cell #\(indexPath.item)!")
        delegate?.open(translate: dataSource[indexPath.row])
    }
    
}

extension HistoryPageVC: UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: screenWidth, height: 70)
    }
    
}

class HistoryViewCell: UICollectionViewCell {
    @IBOutlet weak var bookmarkUIButton: UIButton!
    @IBOutlet weak var textSource: UILabel!
    @IBOutlet weak var textTarget: UILabel!
    @IBOutlet weak var sourceLanguageCodeUILabel: UILabel!
    @IBOutlet weak var targetLanguageCodeUILabel: UILabel!
}

//
//  HistoryVC.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import UIKit
import shared

class HistoryVC: UIViewController {
    
    @IBOutlet weak var historyUIButton: UIButton!
    @IBOutlet weak var favoritesUIButton: UIButton!
    @IBOutlet weak var clearUIButton: UIButton!
    @IBOutlet weak var tabsUIView: UIView!
    @IBOutlet weak var pageControllerContainer: UIView!
    
    private var viewModel: HistoryViewModel!
    weak var delegate: OpenTranslateDelegate?
    var pageViewController: UIPageViewController!
    var pageViewControllerList: [UIViewController] = []
    private var currentIndex: Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        viewModel = HistoryViewModel()
        
        let statusBarHeight: CGFloat = UIApplication.shared.statusBarFrame.size.height + 10
        let statusbarView = UIView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.size.width, height: statusBarHeight))
        statusbarView.backgroundColor = UIColor.colorPrimary
        view.addSubview(statusbarView)
        
        tabsUIView.backgroundColor = UIColor.colorPrimary
        
        setupPageController()
    }
    
    private func setupPageController() {
        pageViewControllerList = [HistoryPageVC.getInstance(index: 0, viewModel: viewModel, delegate: delegate),
            HistoryPageVC.getInstance(index: 1, viewModel: viewModel, delegate: delegate)
        ]
        pageViewController = UIPageViewController(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        pageViewController.dataSource = self
        pageViewController.delegate = self
        pageViewController.view.frame = CGRect(x: 0,y: 0,width: pageControllerContainer.frame.width,height: pageControllerContainer.frame.height)
        
        self.addChild(self.pageViewController)
        pageControllerContainer.addSubview(pageViewController.view)
        openHistoryPage()
        pageViewController.didMove(toParent: self)
    }
    
    private func openHistoryPage() {
        pageViewController.setViewControllers([pageViewControllerList[0]], direction: .reverse, animated: true, completion: nil)
        updateTabs(historySelected: true)
        currentIndex = 0
    }
    
    private func openFavoritesPage() {
        pageViewController.setViewControllers([pageViewControllerList[1]], direction: .forward, animated: true, completion: nil)
        updateTabs(historySelected: false)
        currentIndex = 1
    }
    
    private func updateTabs(historySelected: Bool) {
        var attributes: [NSAttributedString.Key : Any] = self.historyUIButton.titleLabel!.attributedText!.attributes(at: 0, effectiveRange: nil)
        
        attributes[.foregroundColor] = historySelected ? UIColor.black : UIColor.darkGray
        self.historyUIButton.setAttributedTitle(NSAttributedString(string: MR.strings().view_pager_history.localized(), attributes: attributes), for: .normal)
        
        attributes[.foregroundColor] = historySelected ? UIColor.darkGray : UIColor.black
        self.favoritesUIButton.setAttributedTitle(NSAttributedString(string: MR.strings().view_pager_favorites.localized(), attributes: attributes), for: .normal)
    }
    
    @IBAction func historyUIButtonTapped(_ sender: UIButton) {
        openHistoryPage()
    }
    
    @IBAction func favoritesUIButtonTapped(_ sender: UIButton) {
        openFavoritesPage()
    }
    
    @IBAction func clearUIButtonTapped(_ sender: UIButton) {
        let clearHistory = self.currentIndex == 0
        let title = if clearHistory {
            MR.strings().view_pager_history.localized()
        } else {
            MR.strings().view_pager_favorites.localized()
        }
        let message = if clearHistory {
            MR.strings().delete_history_question.localized()
        } else {
            MR.strings().delete_favorites_question.localized()
        }
        
        let alertController = UIAlertController(title: title, message: message, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: MR.strings().ok.localized(), style: .default, handler: { _ in
            if self.currentIndex == 0 {
                self.viewModel.clearHistory()
            } else {
                self.viewModel.clearFavorites()
            }
        }))
        alertController.addAction(UIAlertAction(title: MR.strings().cancel.localized(), style: .default, handler: { _ in
        }))
        self.present(alertController, animated: true, completion: nil)
    }
    
}

extension HistoryVC {

    override func didMove(toParent parent: UIViewController?) {
        if (parent == nil) {
            viewModel.onCleared()
        }
    }
    
}

extension HistoryVC: UIPageViewControllerDataSource, UIPageViewControllerDelegate {
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController? {
        let indexOfCurrentPageViewController = pageViewControllerList.firstIndex(of: viewController)!
        if indexOfCurrentPageViewController == 0 {
            return nil
        } else {
            return pageViewControllerList[indexOfCurrentPageViewController - 1]
        }
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        let indexOfCurrentPageViewController = pageViewControllerList.firstIndex(of: viewController)!
        if indexOfCurrentPageViewController == pageViewControllerList.count - 1 {
            return nil
        } else {
            return pageViewControllerList[indexOfCurrentPageViewController + 1]
        }
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, didFinishAnimating finished: Bool, previousViewControllers: [UIViewController], transitionCompleted completed: Bool) {
        if completed, let vc = previousViewControllers.first {
            updateTabs(historySelected: pageViewControllerList.firstIndex(of: vc) == 1)
            currentIndex = 1 - pageViewControllerList.firstIndex(of: vc)!
        }
    }
}

//
//  InputWidget.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 10.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import UIKit
import shared

protocol InputWidgetDelegate: AnyObject {
    func textCleared()
    func textChanged(_ text: String)
}

class InputWidget: UIView {
    
    var delegate: InputWidgetDelegate?
    
    var placeholderLabel : UILabel!
    @IBOutlet weak var borderView: UIView!
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var micButton: UIButton!
    @IBOutlet weak var soundButton: UIButton!
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        viewInit()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        viewInit()
    }
    
    func viewInit() {
        let inputWidgetView = Bundle.main.loadNibNamed("InputWidget", owner: self, options: nil)!.first as! UIView
        inputWidgetView.frame = self.bounds
        addSubview(inputWidgetView)
        
        borderView.layer.borderColor = UIColor.lightGray.cgColor
        borderView.layer.borderWidth = 1
        
        textView.text = ""
        
        textView.delegate = self
        placeholderLabel = UILabel()
        placeholderLabel.text = MR.strings().hint_enter_text.localized()
        placeholderLabel.font = .italicSystemFont(ofSize: (textView.font?.pointSize)!)
        placeholderLabel.sizeToFit()
        textView.addSubview(placeholderLabel)
        placeholderLabel.frame.origin = CGPoint(x: 5, y: (textView.font?.pointSize)! / 2)
        placeholderLabel.textColor = .tertiaryLabel
        placeholderLabel.isHidden = !textView.text.isEmpty
    }
    
    @IBAction func clearButtonTapped(_ sender: Any) {
        textView.text = ""
        placeholderLabel?.isHidden = false
        delegate?.textCleared()
    }
    
    @IBAction func micButtonTapped(_ sender: Any) {
        showNotImplementedAlert()
    }
    
    @IBAction func soundButtonTapped(_ sender: Any) {
        showNotImplementedAlert()
    }
    
    func showNotImplementedAlert() {
        let alertController = UIAlertController(title: "", message: MR.strings().not_implemented.localized(), preferredStyle: .actionSheet)
        alertController.addAction(UIAlertAction(title: MR.strings().ok.localized(), style: .default, handler: nil))
        self.window?.rootViewController?.present(alertController, animated: true, completion: nil)
    }
    
}

extension InputWidget : UITextViewDelegate {
    
    func textViewDidChange(_ textView: UITextView) {
        placeholderLabel?.isHidden = !textView.text.isEmpty
    }
    
    func textViewDidEndEditing(_ textView: UITextView) {
        placeholderLabel?.isHidden = !textView.text.isEmpty
        delegate?.textChanged(textView.text)
    }
    
    func textViewDidBeginEditing(_ textView: UITextView) {
        placeholderLabel?.isHidden = true
    }
    
}


//
//  String.swift
//  iosApp
//
//  Created by Aleksei Khokkrin on 08.10.2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import shared

extension StringResource {
    func localized() -> String {
        return self.desc().localized()
    }
}

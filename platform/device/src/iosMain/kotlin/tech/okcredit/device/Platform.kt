package tech.okcredit.device

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val brandName: String
        get() = UIDevice.currentDevice.name
    override val model: String
        get() = UIDevice.currentDevice.model
}

actual fun getPlatform(): Platform = IOSPlatform()

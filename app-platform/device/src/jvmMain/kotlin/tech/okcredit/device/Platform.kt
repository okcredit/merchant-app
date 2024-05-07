package tech.okcredit.device

class IOSPlatform : Platform {
    override val name: String = ""
    override val brandName: String
        get() = "UIDevice.currentDevice.name"
    override val model: String
        get() = "UIDevice.currentDevice.model"
}

actual fun getPlatform(): Platform = IOSPlatform()

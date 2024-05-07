package tech.okcredit.device

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val brandName: String = android.os.Build.BRAND
    override val model: String
        get() = android.os.Build.MODEL
}

actual fun getPlatform(): Platform = AndroidPlatform()

package okcredit.base

import java.util.UUID

class DesktopPlatform : Platform {
    override val name: String = ""
}

actual fun getPlatform(): Platform = DesktopPlatform()

actual fun randomUUID(): String = UUID.randomUUID().toString()

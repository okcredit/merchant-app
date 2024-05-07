package tech.okcredit.device

interface Platform {
    val name: String
    val brandName: String
    val model: String
}

expect fun getPlatform(): Platform

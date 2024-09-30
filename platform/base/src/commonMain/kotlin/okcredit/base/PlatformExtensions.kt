package okcredit.base

interface PlatformExtensions {
    fun shareOnWhatsApp(text: String)

    fun shareOnWhatsApp(mobileNumber: String, text: String)
}

const val DEFAULT_WHITELISTED_DOMAINS =
    "okcredit.in,okrelief.in,okshop.in,okstaff.in,okcr.in,staging.okcredit.app,okcredit.app"

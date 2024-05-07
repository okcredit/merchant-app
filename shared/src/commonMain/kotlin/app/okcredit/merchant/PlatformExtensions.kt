package app.okcredit.merchant

import dev.icerock.moko.resources.StringResource

interface PlatformExtensions {
    fun shareOnWhatsApp(text: String)

    fun shareOnWhatsApp(mobileNumber: String, text: String)

    fun openWebUrl(url: String)

    fun localized(stringResource: StringResource): String
}

const val DEFAULT_WHITELISTED_DOMAINS =
    "okcredit.in,okrelief.in,okshop.in,okstaff.in,okcr.in,staging.okcredit.app,okcredit.app"

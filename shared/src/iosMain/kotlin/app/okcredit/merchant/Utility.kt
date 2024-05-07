package app.okcredit.merchant

import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import platform.Foundation.*
import platform.UIKit.*

object Utility {

    enum class RedirectUrlType(val rawValue: String) {
        PRIVACY_POLICY("https://okcredit.in/privacy"),
        TERMS_OF_SERVICE("https://okcredit.in/terms"),
        OK_CREDIT("https://www.okcredit.com/"),
        GOOGLE_URL("www.google.com"),
    }

    fun routeToPrivacyPolicy(urlType: RedirectUrlType) {
        openWebUrl(urlType.rawValue)
    }

    fun openWebUrl(url: String) {
        val nsUrl = NSURL(string = url)
        if (UIApplication.sharedApplication.canOpenURL(nsUrl)) {
            UIApplication.sharedApplication.openURL(nsUrl)
        }
    }

    fun shareOnWhatsApp(text: String) {
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "wa.me",
            pathSegments = listOf(),
            parameters = Parameters.build { append("text", text) },
        )

        val urlWhats = urlBuilder.build().toString()
        val whatsappURL = NSURL(string = urlWhats)

        if (UIApplication.sharedApplication.canOpenURL(whatsappURL)) {
            UIApplication.sharedApplication.openURL(whatsappURL)
        } else {
            // Handle a problem
        }
    }

    fun canOpenWhatsapp(): Boolean {
        val urlWhats = "whatsapp://send?text=can open whatsapp"
        val whatsappURL = NSURL(string = urlWhats)

        if (UIApplication.sharedApplication.canOpenURL(whatsappURL)) {
            return true
        } else {
            // Handle a problem
        }

        return false
    }
}

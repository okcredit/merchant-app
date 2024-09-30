package okcredit.base

import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import me.tatarka.inject.annotations.Inject
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Inject
class IosPlatformExtensions : PlatformExtensions {

    override fun shareOnWhatsApp(text: String) {
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

    override fun shareOnWhatsApp(mobileNumber: String, text: String) {
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "wa.me",
            pathSegments = listOf(mobileNumber),
            parameters = Parameters.build {
                append("text", text)
            },
        )

        val urlWhats = urlBuilder.build().toString()
        val whatsappURL = NSURL(string = urlWhats)

        if (UIApplication.sharedApplication.canOpenURL(whatsappURL)) {
            UIApplication.sharedApplication.openURL(whatsappURL)
        } else {
            // Handle a problem
        }
    }

    private fun isThirdPartyUrl(url: String): Boolean {
        val uri = URLBuilder(url)
        val host = uri.host

        val domainsArray = DEFAULT_WHITELISTED_DOMAINS.split(",")
        domainsArray.forEach {
            if (host.contains(it, true)) {
                return false
            }
        }
        return true
    }
}

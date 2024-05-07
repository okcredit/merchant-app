package app.okcredit.merchant

import android.content.Context
import android.content.Intent
import android.net.Uri
import app.okcredit.merchant.DEFAULT_WHITELISTED_DOMAINS
import app.okcredit.merchant.PlatformExtensions
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import me.tatarka.inject.annotations.Inject

@Inject
class AndroidPlatformExtensions(private val context: Context) : PlatformExtensions {

    override fun shareOnWhatsApp(text: String) {
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "wa.me",
            pathSegments = listOf(),
            parameters = Parameters.build { append("text", text) },
        )

        val urlWhats = urlBuilder.build().toString()
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(urlWhats),
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }

    override fun shareOnWhatsApp(mobileNumber: String, text: String) {
        val urlBuilder = URLBuilder(
            protocol = URLProtocol.HTTPS,
            host = "wa.me",
            pathSegments = listOf(mobileNumber),
            parameters = Parameters.build { append("text", text) },
        )

        val urlWhats = urlBuilder.build().toString()
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(urlWhats),
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        )
    }

    override fun openWebUrl(url: String) {
        if (isThirdPartyUrl(url)) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url),
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        } else {
            val urlBuilder = URLBuilder(
                protocol = URLProtocol("okcredit", 0),
                host = "web",
                pathSegments = listOf(),
                parameters = Parameters.build { append("url", url) },
            )
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(urlBuilder.build().toString()),
                ).setPackage(context.packageName).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        }
    }

    override fun localized(stringResource: StringResource): String {
        return StringDesc.Resource(stringResource).toString(context)
    }

    private fun isThirdPartyUrl(url: String): Boolean {
        val uri = Uri.parse(Uri.decode(url))
        val host = uri.host ?: ""

        val domainsArray = DEFAULT_WHITELISTED_DOMAINS.split(",")
        domainsArray.forEach {
            if (host.contains(it, true)) {
                return false
            }
        }
        return true
    }
}

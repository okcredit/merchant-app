package okcredit.base

import android.content.Context
import android.content.Intent
import android.net.Uri
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
}

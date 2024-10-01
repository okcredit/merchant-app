package app.okcredit.web.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.jsbridge.processParams
import com.multiplatform.webview.web.WebViewNavigator
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class WhatsAppMessageHandler(
    private val onWhatsAppRequest: (WhatsAppRequest) -> Unit,
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        val param = processParams<WhatsAppRequest>(message)
        onWhatsAppRequest(param)
        callback("")
    }

    override fun methodName(): String {
        return "whatsapp"
    }
}

@Serializable
data class WhatsAppRequest(
    @SerialName("mobile")
    val mobile: String,
    @SerialName("message")
    val message: String,
    @SerialName("image_url")
    val imageUrl: String? = null,
)

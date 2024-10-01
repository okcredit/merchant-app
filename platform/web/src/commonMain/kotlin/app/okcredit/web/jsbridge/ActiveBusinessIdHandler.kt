package app.okcredit.web.jsbridge

import com.multiplatform.webview.jsbridge.IJsMessageHandler
import com.multiplatform.webview.jsbridge.JsMessage
import com.multiplatform.webview.web.WebViewNavigator

class ActiveBusinessIdHandler(
    private val activeBusinessId: () -> String,
) : IJsMessageHandler {
    override fun handle(
        message: JsMessage,
        navigator: WebViewNavigator?,
        callback: (String) -> Unit,
    ) {
        callback(activeBusinessId())
    }

    override fun methodName(): String {
        return "activeBusinessId"
    }
}

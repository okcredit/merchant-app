package app.okcredit.web.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.okcredit.web.jsbridge.ActiveBusinessIdHandler
import app.okcredit.web.jsbridge.AuthV3MessageHandler
import app.okcredit.web.jsbridge.WhatsAppMessageHandler
import cafe.adriel.voyager.core.screen.Screen
import com.multiplatform.webview.jsbridge.rememberWebViewJsBridge
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import okcredit.base.di.rememberScreenModel

data class WebScreen(val url: String) : Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<WebScreenScreenModel>()

        val state by screenModel.states.collectAsState()

        val webState = rememberWebViewState(url = url)
        LaunchedEffect(Unit) {
            webState.webSettings.apply {
                logSeverity = KLogSeverity.Debug
            }
        }


        val jsBridge = rememberWebViewJsBridge()
        LaunchedEffect(jsBridge) {
            jsBridge.register(AuthV3MessageHandler())
            jsBridge.register(ActiveBusinessIdHandler { state.activeBusinessId })
            jsBridge.register(WhatsAppMessageHandler { request ->
                // handle whatsapp request
                screenModel.pushIntent(
                    WebContract.Intent.OnWhatsAppRequest(
                        mobile = request.mobile,
                        message = request.message,
                        imageUrl = request.imageUrl
                    )
                )
            })
        }
        WebView(
            state = webState,
            webViewJsBridge = jsBridge
        )
    }
}
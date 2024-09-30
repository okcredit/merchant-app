package app.okcredit.web

import app.okcredit.web.ui.WebScreen
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import me.tatarka.inject.annotations.Inject

data class Web(val url: String) : ScreenProvider

@Inject
class WebScreenRegistryProvider {

    fun screenRegistry() = screenModule {
        register<Web> {
            WebScreen(it.url)
        }
    }
}
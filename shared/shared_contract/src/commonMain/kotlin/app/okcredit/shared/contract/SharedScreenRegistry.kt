package app.okcredit.shared.contract

import cafe.adriel.voyager.core.registry.ScreenProvider

sealed class SharedScreenRegistry : ScreenProvider {
    data object Home : SharedScreenRegistry()

    data object SyncData : SharedScreenRegistry()

    data object Splash : SharedScreenRegistry()

    data object SelectBusiness : SharedScreenRegistry()
}

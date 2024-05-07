package tech.okcredit.identity

import me.tatarka.inject.annotations.Provides
import tech.okcredit.identity.local.IdentitySettingsFactory

interface DesktopIdentityComponent {

    @Provides
    fun DesktopIdentitySettingsFactory.binds(): IdentitySettingsFactory {
        return this
    }

    @Provides
    fun DesktopIdentityDriverFactory.bind(): IdentityDriverFactory = this
}

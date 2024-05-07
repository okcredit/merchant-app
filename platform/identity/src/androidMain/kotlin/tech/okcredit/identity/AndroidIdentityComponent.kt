package tech.okcredit.identity

import me.tatarka.inject.annotations.Provides
import tech.okcredit.identity.di.IdentityComponent
import tech.okcredit.identity.local.IdentitySettingsFactory

interface AndroidIdentityComponent : IdentityComponent {

    @Provides
    fun AndroidIdentitySettingsFactory.binds(): IdentitySettingsFactory {
        return this
    }

    @Provides
    fun AndroidIdentityDriverFactory.bind(): IdentityDriverFactory = this
}

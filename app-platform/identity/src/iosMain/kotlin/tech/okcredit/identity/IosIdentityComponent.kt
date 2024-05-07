package tech.okcredit.identity

import me.tatarka.inject.annotations.Provides
import tech.okcredit.identity.di.IdentityComponent
import tech.okcredit.identity.local.IdentitySettingsFactory

interface IosIdentityComponent : IdentityComponent {

    @Provides
    fun IosIdentitySettingsFactory.binds(): IdentitySettingsFactory {
        return this
    }

    @Provides
    fun IosIdentityDriverFactory.bind(): IdentityDriverFactory = this
}

package tech.okcredit.identity

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import tech.okcredit.identity.di.IdentityComponent
import tech.okcredit.identity.di.IdentityDriverFactory
import tech.okcredit.identity.local.IdentityDatabase
import tech.okcredit.identity.local.IdentitySettingsFactory

interface IosIdentityComponent : IdentityComponent {

    @Provides
    fun IosIdentitySettingsFactory.binds(): IdentitySettingsFactory {
        return this
    }

    @Provides
    fun provideIdentityDriverFactory(): IdentityDriverFactory {
        return IosSqlDriverFactory(
            schema = IdentityDatabase.Schema,
            name = "okcredit_identity.db",
        )
    }
}

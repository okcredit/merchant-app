package tech.okcredit.identity

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.JvmSqlDriverFactory
import tech.okcredit.identity.di.IdentityDriverFactory
import tech.okcredit.identity.local.IdentityDatabase
import tech.okcredit.identity.local.IdentitySettingsFactory

interface DesktopIdentityComponent {

    @Provides
    fun DesktopIdentitySettingsFactory.binds(): IdentitySettingsFactory {
        return this
    }

    @Provides
    fun provideIdentityDriverFactory(): IdentityDriverFactory {
        return JvmSqlDriverFactory {
            IdentityDatabase.Schema.create(it)
        }
    }
}

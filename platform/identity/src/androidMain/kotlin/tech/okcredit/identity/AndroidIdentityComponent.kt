package tech.okcredit.identity

import android.content.Context
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.AndroidSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.identity.di.IdentityComponent
import tech.okcredit.identity.di.IdentityDriverFactory
import tech.okcredit.identity.local.IdentityDatabase
import tech.okcredit.identity.local.IdentitySettingsFactory

@ContributesTo(AppScope::class)
interface AndroidIdentityComponent : IdentityComponent {

    @Provides
    fun AndroidIdentitySettingsFactory.binds(): IdentitySettingsFactory {
        return this
    }

    @Provides
    fun provideIdentityDriverFactory(context: Context): IdentityDriverFactory {
        return AndroidSqlDriverFactory(
            context = context,
            schema = IdentityDatabase.Schema,
            name = "okcredit_identity.db",
        )
    }
}

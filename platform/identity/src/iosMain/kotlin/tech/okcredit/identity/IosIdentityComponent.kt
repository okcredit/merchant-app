package tech.okcredit.identity

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.identity.di.IdentityComponent
import tech.okcredit.identity.di.IdentityDriverFactory
import tech.okcredit.identity.local.IdentityDatabase

@ContributesTo(AppScope::class)
interface IosIdentityComponent : IdentityComponent {

    @Provides
    fun provideIdentityDriverFactory(): IdentityDriverFactory {
        return IosSqlDriverFactory(
            schema = IdentityDatabase.Schema,
            name = "okcredit_identity.db",
        )
    }
}

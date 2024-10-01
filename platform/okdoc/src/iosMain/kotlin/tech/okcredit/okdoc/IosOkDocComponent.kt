package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.okdoc.di.OkDocDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase

@ContributesTo(AppScope::class)
interface IosOkDocComponent {

    @Provides
    fun provideOkDocDriverFactory(): OkDocDriverFactory {
        return IosSqlDriverFactory(
            schema = OkDocDatabase.Schema,
            name = "okcredit_okdoc.db",
        )
    }
}

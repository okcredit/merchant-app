package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import tech.okcredit.okdoc.di.OkDocDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase

interface IosOkDocComponent {

    @Provides
    fun provideOkDocDriverFactory(): OkDocDriverFactory {
        return IosSqlDriverFactory(
            schema = OkDocDatabase.Schema,
            name = "okcredit_okdoc.db",
        )
    }
}

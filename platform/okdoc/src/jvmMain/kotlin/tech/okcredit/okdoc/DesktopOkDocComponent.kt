package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.JvmSqlDriverFactory
import tech.okcredit.okdoc.di.OkDocDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase

interface DesktopOkDocComponent {

    @Provides
    fun provideOkDocDriverFactory(): OkDocDriverFactory {
        return JvmSqlDriverFactory {
            OkDocDatabase.Schema.create(it)
        }
    }
}

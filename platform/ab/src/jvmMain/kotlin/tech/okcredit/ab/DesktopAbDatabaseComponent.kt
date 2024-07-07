package tech.okcredit.ab

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.JvmSqlDriverFactory
import tech.okcredit.ab.di.AbDriverFactory
import tech.okcredit.ab.local.AbDatabase

interface DesktopAbDatabaseComponent {

    @Provides
    fun provideAbDriverFactory(): AbDriverFactory {
        return JvmSqlDriverFactory {
            AbDatabase.Schema.create(it)
        }
    }
}

package tech.okcredit.okdoc.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase
import tech.okcredit.okdoc.local.OkDocDatabaseQueries

typealias OkDocDriverFactory = SqlDriverFactory

interface OkDocComponent {

    @Provides
    fun okDocDatabase(driverFactory: OkDocDriverFactory): OkDocDatabaseQueries {
        val database = OkDocDatabase(driverFactory.createDriver())
        return database.okDocDatabaseQueries
    }
}

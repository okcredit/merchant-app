package tech.okcredit.ab.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.ab.local.AbDatabase
import tech.okcredit.ab.local.AbDatabaseQueries

typealias AbDriverFactory = SqlDriverFactory

interface AbComponent {

    @Provides
    fun abDatabaseDao(abDriverFactory: AbDriverFactory): AbDatabaseQueries {
        val driver = abDriverFactory.createDriver()
        val database = AbDatabase(driver)
        return database.abDatabaseQueries
    }
}

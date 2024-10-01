package tech.okcredit.identity.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.identity.local.IdentityDatabase
import tech.okcredit.identity.local.IdentityDatabaseQueries

typealias IdentityDriverFactory = SqlDriverFactory

interface IdentityComponent {

    @Provides
    fun identityDatabase(driverFactory: IdentityDriverFactory): IdentityDatabaseQueries {
        val database = IdentityDatabase(driverFactory.createDriver())
        return database.identityDatabaseQueries
    }
}

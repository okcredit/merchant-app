package tech.okcredit.okdoc.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase
import tech.okcredit.okdoc.local.OkDocDatabaseQueries
import tech.okcredit.okdoc.remote.OkDocApiClient

typealias OkDocDriverFactory = SqlDriverFactory

interface OkDocComponent {

    @Provides
    fun okDocDatabase(driverFactory: OkDocDriverFactory): OkDocDatabaseQueries {
        val database = OkDocDatabase(driverFactory.createDriver())
        return database.okDocDatabaseQueries
    }

    @Provides
    fun okDocApiClient(ktorfit: Ktorfit): OkDocApiClient = ktorfit.create()
}

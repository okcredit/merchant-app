package tech.okcredit.ab.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import okcredit.base.syncer.AppInitializer
import okcredit.base.syncer.SignOutListener
import tech.okcredit.ab.AbAppInitializer
import tech.okcredit.ab.AbDriverFactory
import tech.okcredit.ab.AbRepository
import tech.okcredit.ab.AbRepositoryImpl
import tech.okcredit.ab.AbSignOutListener
import tech.okcredit.ab.local.AbDatabase
import tech.okcredit.ab.local.AbDatabaseQueries
import tech.okcredit.ab.remote.AbApiClient
import tech.okcredit.ab.remote.createAbApiClient

interface AbComponent {

    @Provides
    fun abApiClient(ktorfit: Ktorfit): AbApiClient {
        return ktorfit.createAbApiClient()
    }

    @Provides
    fun abDatabaseDao(abDriverFactory: AbDriverFactory): AbDatabaseQueries {
        val driver = abDriverFactory.createDriver()
        val database = AbDatabase(driver)
        return database.abDatabaseQueries
    }

    @Provides
    fun repository(abRepositoryImpl: AbRepositoryImpl): AbRepository {
        return abRepositoryImpl
    }

    @Provides
    @IntoSet
    fun abSignOutListener(listener: AbSignOutListener): SignOutListener {
        return listener
    }

    @Provides
    @IntoSet
    fun abAppInitializer(listener: AbAppInitializer): AppInitializer {
        return listener
    }
}

package app.okcredit.staff_link.di

import app.okcredit.staff_link.data.local.StaffLinkSqlDriver
import app.okcredit.staff_link.data.remote.StaffLinkApiService
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.SqlDriverFactory
import okcredit.base.network.KtorfitFactory

typealias StaffLinkDriverFactory = SqlDriverFactory

interface StaffLinkComponent {


    @Provides
    fun staffLinkDriver(factory: StaffLinkDriverFactory): StaffLinkSqlDriver {
        return factory.createDriver()
    }

    @Provides
    fun apiClient(ktorfitFactory: KtorfitFactory): StaffLinkApiService {
        return ktorfitFactory.create().create()
    }
}
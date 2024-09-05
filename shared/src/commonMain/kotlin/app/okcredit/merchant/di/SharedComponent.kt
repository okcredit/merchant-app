package app.okcredit.merchant.di

import app.okcredit.merchant.home.HomeScreenModel
import app.okcredit.merchant.ledger.HomeLedgerScreenModel
import app.okcredit.merchant.search.HomeSearchScreenModel
import app.okcredit.merchant.selectBusiness.SelectBusinessScreenModel
import app.okcredit.merchant.splash.SplashScreenModel
import app.okcredit.merchant.sync.SyncScreenModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair

interface SharedComponent {

    @Provides
    @IntoMap
    fun splashModel(screenModel: SplashScreenModel): ScreenModelPair {
        return SplashScreenModel::class to screenModel
    }

    @Provides
    @IntoMap
    fun homeScreenModel(homeScreenModel: HomeScreenModel): ScreenModelPair {
        return HomeScreenModel::class to homeScreenModel
    }

    @Provides
    @IntoMap
    fun ledgerScreenModel(screenModel: HomeLedgerScreenModel): ScreenModelPair {
        return HomeLedgerScreenModel::class to screenModel
    }

    @Provides
    @IntoMap
    fun syncScreenModel(syncScreenModel: SyncScreenModel): ScreenModelPair {
        return SyncScreenModel::class to syncScreenModel
    }

    @Provides
    @IntoMap
    fun selectBusinessScreenModel(screenModel: SelectBusinessScreenModel): ScreenModelPair {
        return SelectBusinessScreenModel::class to screenModel
    }

    @Provides
    @IntoMap
    fun homeSearchScreenModel(screenModel: HomeSearchScreenModel): ScreenModelPair {
        return HomeSearchScreenModel::class to screenModel
    }
}

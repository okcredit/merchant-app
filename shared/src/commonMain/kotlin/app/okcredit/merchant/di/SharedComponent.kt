package app.okcredit.merchant.di

import app.okcredit.merchant.ledger.HomeLedgerScreenModel
import app.okcredit.merchant.selectBusiness.SelectBusinessScreenModel
import app.okcredit.merchant.splash.SplashScreenModel
import app.okcredit.merchant.sync.SyncScreenModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair

interface SharedComponent {

    @Provides
    @IntoMap
    fun splashModel(splashScreenModel: SplashScreenModel): ScreenModelPair {
        return SplashScreenModel::class to splashScreenModel
    }

    @Provides
    @IntoMap
    fun homeScreenModel(homeScreenModel: HomeLedgerScreenModel): ScreenModelPair {
        return HomeLedgerScreenModel::class to homeScreenModel
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
}

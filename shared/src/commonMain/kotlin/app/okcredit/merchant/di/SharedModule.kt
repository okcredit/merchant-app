package app.okcredit.merchant.di

import app.okcredit.merchant.home.HomeScreenModel
import app.okcredit.merchant.splash.SplashScreenModel
import app.okcredit.merchant.sync.SyncScreenModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair

interface SharedModule {

    @Provides
    @IntoMap
    fun splashModel(splashScreenModel: SplashScreenModel): ScreenModelPair {
        return SplashScreenModel::class to splashScreenModel
    }

    @Provides
    @IntoMap
    fun homeScreenModel(homeScreenModel: HomeScreenModel): ScreenModelPair {
        return HomeScreenModel::class to homeScreenModel
    }

    @Provides
    @IntoMap
    fun syncScreenModel(syncScreenModel: SyncScreenModel): ScreenModelPair {
        return SyncScreenModel::class to syncScreenModel
    }
}

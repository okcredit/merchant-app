package app.okcredit.merchant

import app.okcredit.merchant.di.SharedModule
import me.tatarka.inject.annotations.Provides

interface IosSharedModule : SharedModule {

    @Provides
    fun provideShareTextHandler(iosPlatformExtensions: IosPlatformExtensions): PlatformExtensions {
        return iosPlatformExtensions
    }
}

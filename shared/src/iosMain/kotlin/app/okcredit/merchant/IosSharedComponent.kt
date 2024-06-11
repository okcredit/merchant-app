package app.okcredit.merchant

import app.okcredit.merchant.di.SharedComponent
import me.tatarka.inject.annotations.Provides

interface IosSharedComponent : SharedComponent {

    @Provides
    fun provideShareTextHandler(iosPlatformExtensions: IosPlatformExtensions): PlatformExtensions {
        return iosPlatformExtensions
    }
}

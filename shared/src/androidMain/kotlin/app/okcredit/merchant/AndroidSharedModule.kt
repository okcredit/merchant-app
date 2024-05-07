package app.okcredit.merchant

import app.okcredit.merchant.PlatformExtensions
import app.okcredit.merchant.di.SharedModule
import me.tatarka.inject.annotations.Provides

interface AndroidSharedModule : SharedModule {

    @Provides
    fun androidPlatformExtensions(platformExtensions: AndroidPlatformExtensions): PlatformExtensions {
        return platformExtensions
    }
}

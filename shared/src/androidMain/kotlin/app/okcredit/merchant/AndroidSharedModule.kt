package app.okcredit.merchant

import app.okcredit.merchant.di.SharedComponent
import me.tatarka.inject.annotations.Provides

interface AndroidSharedModule : SharedComponent {

    @Provides
    fun androidPlatformExtensions(platformExtensions: AndroidPlatformExtensions): PlatformExtensions {
        return platformExtensions
    }
}

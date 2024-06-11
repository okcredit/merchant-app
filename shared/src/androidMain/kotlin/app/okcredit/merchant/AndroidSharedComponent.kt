package app.okcredit.merchant

import app.okcredit.merchant.di.SharedComponent
import me.tatarka.inject.annotations.Provides

interface AndroidSharedComponent : SharedComponent {

    @Provides
    fun AndroidPlatformExtensions.binds(): PlatformExtensions {
        return this
    }
}

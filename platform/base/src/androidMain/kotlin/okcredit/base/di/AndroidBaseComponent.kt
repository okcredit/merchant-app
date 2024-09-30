package okcredit.base.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.AndroidPlatformExtensions
import okcredit.base.PlatformExtensions

interface AndroidBaseComponent : BaseComponent {

    @Provides
    fun AndroidPlatformExtensions.binds(): PlatformExtensions {
        return this
    }
}

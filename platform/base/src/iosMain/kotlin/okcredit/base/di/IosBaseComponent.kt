package okcredit.base.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.IosPlatformExtensions
import okcredit.base.PlatformExtensions

interface IosBaseComponent : BaseComponent {

    @Provides
    fun IosPlatformExtensions.binds(): PlatformExtensions {
        return this
    }
}

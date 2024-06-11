package tech.okcredit.customization.di

import me.tatarka.inject.annotations.Provides
import tech.okcredit.customization.syncer.CustomizationSyncer
import tech.okcredit.customization.syncer.IosCustomizationSyncer

interface IosCustomizationComponent : CustomizationComponent {

    @Provides
    fun IosCustomizationSyncer.bind(): CustomizationSyncer = this

    @Provides
    fun IosCustomizationDriverFactory.bind(): CustomizationDriverFactory = this
}

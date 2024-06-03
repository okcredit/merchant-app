package tech.okcredit.customization.di

import me.tatarka.inject.annotations.Provides

interface JvmCustomizationComponent : CustomizationComponent {

    @Provides
    fun JvmCustomizationDriverFactory.binds(): CustomizationDriverFactory = this
}
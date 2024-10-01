package tech.okcredit.customization.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.customization.local.CustomizationDatabase
import tech.okcredit.customization.syncer.CustomizationSyncer
import tech.okcredit.customization.syncer.IosCustomizationSyncer

@ContributesTo(AppScope::class)
interface IosCustomizationComponent : CustomizationComponent {

    @Provides
    fun IosCustomizationSyncer.bind(): CustomizationSyncer = this

    @Provides
    fun provideCustomizationDriverFactory(): CustomizationDriverFactory {
        return IosSqlDriverFactory(
            schema = CustomizationDatabase.Schema,
            name = "okcredit_customization.db",
        )
    }
}

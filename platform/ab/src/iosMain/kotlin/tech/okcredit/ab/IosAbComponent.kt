package tech.okcredit.ab

import me.tatarka.inject.annotations.Provides
import okcredit.base.local.IosSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.ab.di.AbComponent
import tech.okcredit.ab.di.AbDriverFactory
import tech.okcredit.ab.local.AbDatabase

@ContributesTo(AppScope::class)
interface IosAbComponent : AbComponent {

    @Provides
    fun provideAbDriverFactory(): AbDriverFactory {
        return IosSqlDriverFactory(
            schema = AbDatabase.Schema,
            name = "okcredit_ab.db",
        )
    }

    @Provides
    fun IosProfileSyncer.bind(): ProfileSyncer = this

    @Provides
    fun IosExperimentSyncer.bind(): ExperimentSyncer = this
}

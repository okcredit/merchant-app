package tech.okcredit.ab

import android.content.Context
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.AndroidSqlDriverFactory
import okcredit.base.syncer.WorkerFactoryPair
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.ab.di.AbComponent
import tech.okcredit.ab.di.AbDriverFactory
import tech.okcredit.ab.local.AbDatabase

@ContributesTo(AppScope::class)
interface AndroidAbComponent : AbComponent {

    @Provides
    fun provideAbDriverFactory(context: Context): AbDriverFactory {
        return AndroidSqlDriverFactory(
            context = context,
            schema = AbDatabase.Schema,
            name = "okcredit_ab.db",
        )
    }

    @Provides
    fun AndroidProfileSyncer.bind(): ProfileSyncer = this

    @Provides
    fun AndroidExperimentSyncer.bind(): ExperimentSyncer = this

    @Provides
    @IntoMap
    fun experimentAcknowledgeWorker(factory: ExperimentAcknowledgeWorker.Factory): WorkerFactoryPair {
        return ExperimentAcknowledgeWorker::class to factory
    }

    @Provides
    @IntoMap
    fun abSyncWorker(factory: AbSyncWorker.Factory): WorkerFactoryPair {
        return AbSyncWorker::class to factory
    }
}

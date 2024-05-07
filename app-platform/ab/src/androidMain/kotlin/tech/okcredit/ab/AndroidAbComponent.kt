package tech.okcredit.ab

import androidx.work.ListenableWorker
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.Singleton
import okcredit.base.syncer.ChildWorkerFactory
import tech.okcredit.ab.di.AbComponent
import kotlin.reflect.KClass

interface AndroidAbComponent : AbComponent {
    @Provides
    fun AndroidAbDriverFactory.bind(): AbDriverFactory = this

    @Provides
    fun AndroidProfileSyncer.bind(): ProfileSyncer = this

    @Provides
    fun AndroidExperimentSyncer.bind(): ExperimentSyncer = this

    @Provides
    @IntoMap
    @Singleton
    fun experimentAcknowledgeWorker(factory: ExperimentAcknowledgeWorker.Factory): Pair<KClass<out ListenableWorker>, ChildWorkerFactory> {
        return ExperimentAcknowledgeWorker::class to factory
    }

    @Provides
    @IntoMap
    @Singleton
    fun abSyncWorker(factory: AbSyncWorker.Factory): Pair<KClass<out ListenableWorker>, ChildWorkerFactory> {
        return AbSyncWorker::class to factory
    }
}

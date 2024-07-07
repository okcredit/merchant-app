package tech.okcredit.ab

import android.content.Context
import androidx.work.ListenableWorker
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.Singleton
import okcredit.base.local.AndroidSqlDriverFactory
import okcredit.base.syncer.ChildWorkerFactory
import tech.okcredit.ab.di.AbComponent
import tech.okcredit.ab.di.AbDriverFactory
import tech.okcredit.ab.local.AbDatabase
import kotlin.reflect.KClass

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

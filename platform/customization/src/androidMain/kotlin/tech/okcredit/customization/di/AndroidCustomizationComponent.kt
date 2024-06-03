package tech.okcredit.customization.di

import androidx.work.ListenableWorker
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.syncer.ChildWorkerFactory
import tech.okcredit.customization.syncer.AndroidCustomizationSyncer
import tech.okcredit.customization.syncer.CustomizationSyncer
import tech.okcredit.customization.syncer.SyncCustomizationsWorker
import kotlin.reflect.KClass

interface AndroidCustomizationComponent : CustomizationComponent {

    @Provides
    fun AndroidCustomizationSyncer.bind(): CustomizationSyncer = this

    @Provides
    fun AndroidCustomizationDriverFactory.bind(): CustomizationDriverFactory = this

    @Provides
    @IntoMap
    fun syncCustomizationsWorker(factory: SyncCustomizationsWorker.Factory): Pair<KClass<out ListenableWorker>, ChildWorkerFactory> {
        return SyncCustomizationsWorker::class to factory
    }
}

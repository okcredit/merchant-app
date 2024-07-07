package tech.okcredit.customization.di

import android.content.Context
import androidx.work.ListenableWorker
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.AndroidSqlDriverFactory
import okcredit.base.syncer.ChildWorkerFactory
import tech.okcredit.customization.local.CustomizationDatabase
import tech.okcredit.customization.syncer.AndroidCustomizationSyncer
import tech.okcredit.customization.syncer.CustomizationSyncer
import tech.okcredit.customization.syncer.SyncCustomizationsWorker
import kotlin.reflect.KClass

interface AndroidCustomizationComponent : CustomizationComponent {

    @Provides
    fun AndroidCustomizationSyncer.bind(): CustomizationSyncer = this

    @Provides
    fun provideCustomizationDriverFactory(context: Context): CustomizationDriverFactory {
        return AndroidSqlDriverFactory(
            context = context,
            schema = CustomizationDatabase.Schema,
            name = "okcredit_customization.db",
        )
    }

    @Provides
    @IntoMap
    fun syncCustomizationsWorker(factory: SyncCustomizationsWorker.Factory): Pair<KClass<out ListenableWorker>, ChildWorkerFactory> {
        return SyncCustomizationsWorker::class to factory
    }
}

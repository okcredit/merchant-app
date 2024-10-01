package okcredit.base.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject
import kotlin.reflect.KClass

@Inject
class AppWorkerFactory(
    private val workerFactories: Map<KClass<out ListenableWorker>, ChildWorkerFactory> = emptyMap(),
) : WorkerFactory() {
    override fun createWorker(
        context: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return try {
            val foundEntry =
                workerFactories.entries.find { it.key.qualifiedName == workerClassName }
            val factoryProvider = foundEntry?.value
            factoryProvider?.create(context, workerParameters)
        } catch (exception: ClassNotFoundException) {
            null
        }
    }
}

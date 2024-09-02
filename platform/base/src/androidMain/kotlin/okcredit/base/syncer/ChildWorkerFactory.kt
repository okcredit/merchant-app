package okcredit.base.syncer

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import kotlin.reflect.KClass

interface ChildWorkerFactory {
    fun create(context: Context, params: WorkerParameters): ListenableWorker
}

typealias WorkerFactoryPair = Pair<KClass<out ListenableWorker>, ChildWorkerFactory>

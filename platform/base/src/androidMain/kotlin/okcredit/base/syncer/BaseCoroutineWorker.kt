package okcredit.base.syncer

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CancellationException

abstract class BaseCoroutineWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val workerConfig: WorkerConfig,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_INSTRUMENTATION_SAMPLING = "worker_instrumentation_sampling"
    }

    private var outputData: Data? = null

    abstract suspend fun doActualWork()

    final override suspend fun doWork(): Result {
        return try {
            doActualWork()
            successResult()
        } catch (cancellation: CancellationException) {
            failureResult()
            throw cancellation
        } catch (exception: Exception) {
            Logger.w(throwable = exception) { "${exception.message}" }
            if (canRetry()) Result.retry() else failureResult()
        }
    }

    private fun canRetry() = runAttemptCount < workerConfig.maxAttemptCount || workerConfig.allowUnlimitedRun

    private fun successResult() = outputData?.let { outputData -> Result.success(outputData) } ?: Result.success()

    private fun failureResult() = outputData?.let { outputData -> Result.failure(outputData) } ?: Result.failure()

    fun setOutputData(outputData: Data) {
        this.outputData = outputData
    }
}

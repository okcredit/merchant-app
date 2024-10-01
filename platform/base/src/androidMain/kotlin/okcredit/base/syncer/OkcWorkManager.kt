package okcredit.base.syncer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.Operation
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import okcredit.base.local.Scope
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * [OkcWorkManager] is a wrapper class on top of android's WorkManager.
 * It basically manipulates the uniqueWorkName passed for OneTimeWork to support [Scope] (for multiple accounts).
 *
 * PS: Do not use WorkManager anywhere in the app.
 */
@Inject
@SingleIn(AppScope::class)
class OkcWorkManager(
    context: Context,
    private val preferences: Lazy<WorkManagerPrefs>,
) {

    private val workManager by lazy { WorkManager.getInstance(context) }

    fun schedule(
        uniqueWorkName: String,
        scope: Scope,
        existingWorkPolicy: ExistingWorkPolicy,
        workRequest: OneTimeWorkRequest,
    ) {
        val scopedWorkName = Scope.getScopedKey(uniqueWorkName, scope)
        workManager.beginUniqueWork(scopedWorkName, existingWorkPolicy, workRequest).enqueue()
    }

    fun cancelAllWorkByTag(tag: String) = workManager.cancelAllWorkByTag(tag)

    fun cancelAllWork() = workManager.cancelAllWork()

    fun cancelUniqueWork(uniqueWorkName: String, scope: Scope): Operation {
        val scopedWorkName = Scope.getScopedKey(uniqueWorkName, scope)
        return workManager.cancelUniqueWork(scopedWorkName)
    }

    fun getWorkInfoById(id: UUID) = workManager.getWorkInfoById(id)

    fun getWorkInfoByIdLiveData(id: UUID) = workManager.getWorkInfoByIdLiveData(id)

    fun getWorkInfos(workQuery: WorkQuery) = workManager.getWorkInfos(workQuery)

    fun getWorkInfosForUniqueWorkLiveData(uniqueWorkName: String, scope: Scope): LiveData<MutableList<WorkInfo>> {
        val scopedWorkName = Scope.getScopedKey(uniqueWorkName, scope)
        return workManager.getWorkInfosForUniqueWorkLiveData(scopedWorkName)
    }

    fun getWorkInfosForUniqueWork(uniqueWorkName: String, scope: Scope): ListenableFuture<MutableList<WorkInfo>> {
        val scopedWorkName = Scope.getScopedKey(uniqueWorkName, scope)
        return workManager.getWorkInfosForUniqueWork(scopedWorkName)
    }

    /**
     * Function for scheduling one time worker with rate limit.
     * The worker will not be scheduled if the rate limit is not satisfied.
     *
     * PS: Please ensure [uniqueWorkName] is unique across the application,
     * as last scheduled timestamp is stored based on [uniqueWorkName]
     */
    suspend fun scheduleWithRateLimit(
        uniqueWorkName: String,
        scope: Scope,
        existingWorkPolicy: ExistingWorkPolicy,
        workRequest: OneTimeWorkRequest,
        rateLimit: RateLimit,
    ) {
        val scopedWorkName = Scope.getScopedKey(uniqueWorkName, scope)
        val currentTimestampMillis = System.currentTimeMillis()
        val workerLastTriggeredKey = scopedWorkName.plus(RateLimit.KEY_POSTFIX_LAST_TRIGGERED)

        val canSchedule = checkRateLimit(workerLastTriggeredKey, rateLimit, currentTimestampMillis, scope)
        if (canSchedule) {
            schedule(uniqueWorkName, scope, existingWorkPolicy, workRequest)
            updateLastScheduledTimestamp(workerLastTriggeredKey, currentTimestampMillis, scope)
        } else {
            co.touchlab.kermit.Logger.d { "scheduleWithRateLimit: Skipped scheduling [$uniqueWorkName] due to rate limiting" }
        }
    }

    private suspend fun checkRateLimit(
        workerLastTriggeredKey: String,
        rateLimit: RateLimit,
        currentTimestampMillis: Long,
        scope: Scope,
    ): Boolean {
        if (rateLimit.limit == 0L) return true

        val lastScheduled = preferences.value.getLong(workerLastTriggeredKey, scope).first()
        return (currentTimestampMillis - lastScheduled) > rateLimit.millis
    }

    private suspend fun updateLastScheduledTimestamp(
        workerLastTriggeredKey: String,
        currentTimestampMillis: Long,
        scope: Scope,
    ) = preferences.value.set(workerLastTriggeredKey, currentTimestampMillis, scope)
}

/**
 * [RateLimit] can be used to limit the number of times a worker is scheduled
 *
 * Eg. If the worker should run once in 3 days, the rate limit should be : RateLimit(3, TimeUnit.DAYS)
 */
data class RateLimit(
    val limit: Long,
    val unit: TimeUnit,
) {
    companion object {
        const val KEY_POSTFIX_LAST_TRIGGERED = "_last_triggered"
        const val FRC_KEY_NON_CRITICAL_DATA_WORKER_RATE_LIMIT_HOURS = "non_critical_data_worker_rate_limit_hours"
    }

    val millis
        get() = TimeUnit.MILLISECONDS.convert(limit, unit)
}

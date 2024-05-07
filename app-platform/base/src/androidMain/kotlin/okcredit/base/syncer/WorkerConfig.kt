package okcredit.base.syncer

data class WorkerConfig(
    val label: String,
    val allowUnlimitedRun: Boolean = false,
    val maxAttemptCount: Int = 3,
) {

    companion object {
        const val WORKER_MAXIMUM_ATTEMPT_KEY = "worker_maximum_attempt"
    }
}

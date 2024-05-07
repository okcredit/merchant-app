package tech.okcredit.okdoc.usecase

import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import tech.okcredit.okdoc.OkDocUploadManager
import tech.okcredit.okdoc.local.FileUploadCommand
import tech.okcredit.okdoc.local.FileUploadStatus
import tech.okcredit.okdoc.local.OkDocLocalSource

@Inject
class OkDocUploadFile(
    private val localSource: OkDocLocalSource,
    private val okDocUploadManager: OkDocUploadManager,
) {
    private fun generateRemoteUrl(
        requestId: String,
        useCase: OkDocUseCase,
        fileExtension: OkDocFileExtension,
    ): String {
        return "https://storage.googleapis.com/okdoc-production/${useCase.value}/$requestId.${fileExtension.value}"
    }

    suspend fun schedule(
        requestId: String,
        useCase: OkDocUseCase,
        localFilePath: String,
        fileExtension: OkDocFileExtension,
        businessId: String,
    ): String {
        val remoteUrl = generateRemoteUrl(requestId, useCase, fileExtension)
        val command = FileUploadCommand(
            commandId = requestId,
            businessId = businessId,
            type = useCase.value,
            filePath = localFilePath,
            createdTime = Clock.System.now().epochSeconds,
            errorCode = 0,
            expiryTime = Long.MAX_VALUE,
            remoteUrl = remoteUrl,
            runAttemptCount = 0L,
            status = FileUploadStatus.ACCEPTED.name,
        )
        localSource.insertCommand(command)

        okDocUploadManager.schedule()
        return remoteUrl
    }
}

enum class OkDocUseCase(val value: String) {
    TRANSACTION_IMAGE("transaction_image"),
    PROFILE_IMAGE("profile_image"),
    AUDIO_SAMPLE("voice_samples"),
    UNKNOWN("unknown"),
}

enum class OkDocFileExtension(val value: String) {
    IMAGE(".jpg"),
    AUDIO_MP3(".mp3"),
    AUDIO_WAV(".wav"),
    PDF(".pdf"),
}

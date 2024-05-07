package tech.okcredit.okdoc

import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.OneTimeDataSyncer
import okio.Path
import okio.Path.Companion.toPath
import tech.okcredit.okdoc.local.FileUploadCommand
import tech.okcredit.okdoc.local.FileUploadStatus
import tech.okcredit.okdoc.local.OkDocLocalSource
import tech.okcredit.okdoc.remote.OkDocApiClient

typealias OkDocSyncer = OneTimeDataSyncer

private fun String.toStatus(): FileUploadStatus {
    return FileUploadStatus.valueOf(this)
}

@Inject
class OkDocUploadManager(
    private val localSource: OkDocLocalSource,
    private val apiClient: OkDocApiClient,
    private val okDocSyncer: OkDocSyncer,
) {

    companion object {
        const val MAX_RUN_ATTEMPT_COUNT = 3L
    }

    private val executeLock = Mutex()
    private val scheduleLock = Mutex()

    suspend fun execute() {
        executeLock.withLock {
            val pendingCommands = localSource.getPendingFilesForUpload()
            pendingCommands.forEach {
                uploadPendingFile(it)
            }
        }
    }

    suspend fun schedule() {
        scheduleLock.withLock {
            okDocSyncer.schedule(JsonObject(mapOf()))
        }
    }

    private suspend fun uploadPendingFile(command: FileUploadCommand) {
        if (!fileExists(command.filePath)) {
            localSource.updatePendingCommandStatus(command.commandId, FileUploadStatus.FAILED)
            return
        }

        val latestStatus = localSource.getStatusForCommand(command.commandId).toStatus()
        if (latestStatus != FileUploadStatus.ACCEPTED) return

        if (command.runAttemptCount == MAX_RUN_ATTEMPT_COUNT) return

        uploadToServer(command)
    }

    private suspend fun uploadToServer(command: FileUploadCommand) {
        val multipart = formData {
            append(
                key = "file",
                value = fileBytes(command.filePath.toPath()),
                headers = Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data")
                    append(HttpHeaders.ContentDisposition, "filename=ktor_logo.png")
                },
            )
        }

        localSource.updatePendingCommandStatus(command.commandId, FileUploadStatus.IN_PROGRESS)
        apiClient.uploadFile(
            businessId = command.businessId,
            requestId = command.commandId,
            useCase = command.type,
            isSecured = false,
            file = multipart,
        )
    }
}

expect fun fileExists(filePath: String): Boolean

expect fun fileBytes(path: Path): ByteArray

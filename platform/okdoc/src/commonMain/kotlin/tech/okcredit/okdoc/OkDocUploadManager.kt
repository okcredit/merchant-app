package tech.okcredit.okdoc

import io.ktor.client.plugins.onUpload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.JsonObject
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.BaseUrl
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.randomUUID
import okcredit.base.syncer.OneTimeDataSyncer
import okio.Path
import okio.Path.Companion.toPath
import tech.okcredit.okdoc.local.FileUploadCommand
import tech.okcredit.okdoc.local.FileUploadStatus
import tech.okcredit.okdoc.local.OkDocLocalSource

typealias OkDocSyncer = OneTimeDataSyncer

private fun String.toStatus(): FileUploadStatus {
    return FileUploadStatus.valueOf(this)
}

@Inject
class OkDocUploadManager(
    private val baseUrl: BaseUrl,
    private val localSource: OkDocLocalSource,
    private val okDocSyncer: OkDocSyncer,
    private val authorizedHttpClient: AuthorizedHttpClient,
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
        localSource.updatePendingCommandStatus(
            commandId = command.commandId,
            status = FileUploadStatus.IN_PROGRESS,
        )

        authorizedHttpClient.post(baseUrl + "app/okdoc/v2/file/upload") {
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("is_secured", "false")
                        append("request_id", randomUUID())
                        append("use_case", "unknown")
                        append(
                            key = "file",
                            value = fileBytes(command.filePath.toPath()),
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, "image/png")
                                append(HttpHeaders.ContentDisposition, "filename=ktor_logo.png")
                            },
                        )
                    },
                ),
            )
            onUpload { bytesSentTotal, contentLength ->
                println("Sent $bytesSentTotal bytes from $contentLength")
            }
        }
    }
}

expect fun fileExists(filePath: String): Boolean

expect fun fileBytes(path: Path): ByteArray

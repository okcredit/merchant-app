package tech.okcredit.okdoc.local

import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okcredit.base.di.IoDispatcher

@Inject
class OkDocLocalSource(private val okDocDatabaseQueries: OkDocDatabaseQueries, private val ioDispatcher: IoDispatcher) {

    suspend fun getPendingFilesForUpload(): List<FileUploadCommand> {
        return withContext(ioDispatcher) {
            okDocDatabaseQueries.getPendingCommand().executeAsList()
        }
    }

    suspend fun getStatusForCommand(commandId: String): String {
        return withContext(ioDispatcher) {
            okDocDatabaseQueries.getStatusForCommand(commandId = commandId).executeAsOne()
        }
    }

    suspend fun updatePendingCommandStatus(commandId: String, status: FileUploadStatus) {
        return withContext(ioDispatcher) {
            okDocDatabaseQueries.updateStatusForCommand(status = status.name, commandId = commandId)
        }
    }

    suspend fun insertCommand(command: FileUploadCommand) {
        withContext(ioDispatcher) {
            okDocDatabaseQueries.insertCommand(
                commandId = command.commandId,
                businessId = command.businessId,
                type = command.type,
                filePath = command.filePath,
                createdTime = command.createdTime,
                errorCode = command.errorCode,
                expiryTime = command.expiryTime,
                remoteUrl = command.remoteUrl,
                runAttemptCount = command.runAttemptCount,
                status = FileUploadStatus.ACCEPTED.name,
            )
        }
    }
}

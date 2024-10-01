package tech.okcredit.device.usecase

import me.tatarka.inject.annotations.Inject
import okcredit.base.network.DeviceIdProvider
import okcredit.base.randomUUID
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.device.local.DeviceLocalSource

@Inject
@ContributesBinding(AppScope::class)
class GetOrCreateDeviceId(private val deviceLocalSource: DeviceLocalSource) : DeviceIdProvider {

    override suspend fun current(): String {
        val current = deviceLocalSource.getDevice()
        return current?.id ?: randomUUID() // todo: replace with FID/GUID implementation, refer: https://developer.android.com/training/articles/user-data-ids#instance-ids-guids
    }
}

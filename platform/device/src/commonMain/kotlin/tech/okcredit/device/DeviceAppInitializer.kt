package tech.okcredit.device

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.AppInitializer

@Inject
class DeviceAppInitializer(
    private val deviceRepository: DeviceRepository,
) : AppInitializer {

    @OptIn(DelicateCoroutinesApi::class)
    override fun init() {
        GlobalScope.launch {
            deviceRepository.syncDeviceData()
        }
    }
}

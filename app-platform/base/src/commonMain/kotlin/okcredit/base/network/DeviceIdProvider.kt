package okcredit.base.network

interface DeviceIdProvider {
    suspend fun current(): String
}

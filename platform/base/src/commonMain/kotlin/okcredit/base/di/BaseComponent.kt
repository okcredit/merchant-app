package okcredit.base.di

import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Provides
import okcredit.base.appDispatchers
import okcredit.base.network.DefaultHttpClient
import okcredit.base.network.HttpClientFactory

typealias IoDispatcher = CoroutineDispatcher
typealias MainDispatcher = CoroutineDispatcher

typealias BaseUrl = String
typealias AppVersion = String
typealias AppVersionCode = Int
typealias Debug = Boolean
typealias Flavor = String

@Singleton
interface BaseComponent {

    @Singleton
    @Provides
    fun ioDispatcher(): IoDispatcher = appDispatchers.io

    @Singleton
    @Provides
    fun mainDispatcher(): MainDispatcher = appDispatchers.main

    @Singleton
    @Provides
    fun defaultHttpClient(factory: HttpClientFactory): DefaultHttpClient {
        return factory.createHttpClient()
    }
}

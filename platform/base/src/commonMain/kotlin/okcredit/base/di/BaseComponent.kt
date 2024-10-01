package okcredit.base.di

import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Provides
import okcredit.base.appDispatchers
import okcredit.base.network.DefaultHttpClient
import okcredit.base.network.HttpClientFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

typealias IoDispatcher = CoroutineDispatcher
typealias MainDispatcher = CoroutineDispatcher

typealias BaseUrl = String
typealias AppVersion = String
typealias AppVersionCode = Int
typealias Debug = Boolean
typealias Flavor = String

interface BaseComponent {

    @SingleIn(AppScope::class)
    @Provides
    fun ioDispatcher(): IoDispatcher = appDispatchers.io

    @SingleIn(AppScope::class)
    @Provides
    fun mainDispatcher(): MainDispatcher = appDispatchers.main

    @SingleIn(AppScope::class)
    @Provides
    fun defaultHttpClient(factory: HttpClientFactory): DefaultHttpClient {
        return factory.createHttpClient()
    }
}

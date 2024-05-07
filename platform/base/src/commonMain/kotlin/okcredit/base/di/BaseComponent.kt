package okcredit.base.di

import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Provides
import okcredit.base.appDispatchers
import okcredit.base.network.ClientConfig
import okcredit.base.network.createKtorFit

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
    fun provideKtorfit(
        baseUrl: BaseUrl,
        appVersion: AppVersion,
        debug: Debug,
        clientConfigs: Set<ClientConfig>,
    ): Ktorfit = createKtorFit(
        baseUrl = baseUrl,
        appVersion = appVersion,
        debug = debug,
        clientConfigs = clientConfigs,
    )

    @Singleton
    @Provides
    fun ioDispatcher(): IoDispatcher = appDispatchers.io

    @Singleton
    @Provides
    fun mainDispatcher(): MainDispatcher = appDispatchers.main
}

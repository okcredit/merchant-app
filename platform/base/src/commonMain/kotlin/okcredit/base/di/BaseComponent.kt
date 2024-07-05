package okcredit.base.di

import de.jensklingenberg.ktorfit.Ktorfit
import kotlinx.coroutines.CoroutineDispatcher
import me.tatarka.inject.annotations.Provides
import okcredit.base.appDispatchers
import okcredit.base.network.KtorfitFactory

typealias IoDispatcher = CoroutineDispatcher
typealias MainDispatcher = CoroutineDispatcher

typealias BaseUrl = String
typealias AppVersion = String
typealias AppVersionCode = Int
typealias Debug = Boolean
typealias Flavor = String

@Singleton
interface BaseComponent {

    @Provides
    fun provideKtorfit(
        ktorfitUtils: KtorfitFactory,
    ): Ktorfit = ktorfitUtils.create()

    @Singleton
    @Provides
    fun ioDispatcher(): IoDispatcher = appDispatchers.io

    @Singleton
    @Provides
    fun mainDispatcher(): MainDispatcher = appDispatchers.main
}

package tech.okcredit.auth.di

import me.tatarka.inject.annotations.Provides
import okcredit.base.network.AuthorizedHttpClient
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import tech.okcredit.auth.AuthService
import tech.okcredit.auth.AuthServiceImpl

interface AuthComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun authorizedHttpClient(factory: AuthorizedHttpClientFactory): AuthorizedHttpClient {
        return factory.createAuthorizedHttpClient()
    }

    @Provides
    @SingleIn(AppScope::class)
    fun AuthServiceImpl.binds(): AuthService {
        return this
    }
}

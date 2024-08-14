package tech.okcredit.auth.di

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.Singleton
import okcredit.base.network.AuthorizedHttpClient
import okcredit.base.network.ClientConfig
import okcredit.base.syncer.SignOutListener
import tech.okcredit.auth.AuthService
import tech.okcredit.auth.AuthServiceImpl
import tech.okcredit.auth.AuthSignOutListener

interface AuthComponent {

    @Singleton
    @Provides
    fun AuthServiceImpl.service(): AuthService {
        return this
    }

    @Provides
    @IntoSet
    fun authSignOutListener(listener: AuthSignOutListener): SignOutListener {
        return listener
    }

    @Singleton
    @Provides
    fun authorizedHttpClient(factory: AuthorizedHttpClientFactory): AuthorizedHttpClient {
        return factory.createAuthorizedHttpClient()
    }
}

package tech.okcredit.auth.di

import de.jensklingenberg.ktorfit.Ktorfit
import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.Singleton
import okcredit.base.network.ClientConfig
import okcredit.base.syncer.SignOutListener
import tech.okcredit.auth.AuthService
import tech.okcredit.auth.AuthServiceImpl
import tech.okcredit.auth.AuthSignOutListener
import tech.okcredit.auth.remote.AuthApiClient
import tech.okcredit.auth.remote.Protected

interface AuthComponent {

    @Singleton
    @Provides
    fun authApiClient(ktorfit: Ktorfit): AuthApiClient {
        return ktorfit.create()
    }

    @Singleton
    @Provides
    fun protectedApiClient(ktorfit: Ktorfit): Protected {
        return ktorfit.create()
    }

    @Singleton
    @Provides
    fun AuthServiceImpl.service(): AuthService {
        return this
    }

    @Provides
    @IntoSet
    fun authClientConfig(authClientConfig: AuthClientConfig): ClientConfig {
        return authClientConfig
    }

    @Provides
    @IntoSet
    fun authSignOutListener(listener: AuthSignOutListener): SignOutListener {
        return listener
    }
}

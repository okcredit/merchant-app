package tech.okcredit.auth

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener
import tech.okcredit.auth.local.AuthLocalSource

@Inject
class AuthSignOutListener(
    private val authLocalSourceLazy: Lazy<AuthLocalSource>,
) : SignOutListener {

    private val authLocalSource by lazy { authLocalSourceLazy.value }

    override suspend fun onSignOut() {
        authLocalSource.clearLocalData()
    }
}

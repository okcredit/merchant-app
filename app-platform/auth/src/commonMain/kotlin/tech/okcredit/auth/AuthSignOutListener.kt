package tech.okcredit.auth

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener
import tech.okcredit.auth.local.AuthLocalSource

@Inject
class AuthSignOutListener(private val localSource: AuthLocalSource) : SignOutListener {

    override suspend fun onSignOut() {
        localSource.clearLocalData()
    }
}

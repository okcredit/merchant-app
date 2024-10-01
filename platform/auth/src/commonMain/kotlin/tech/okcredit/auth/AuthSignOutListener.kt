package tech.okcredit.auth

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import tech.okcredit.auth.local.AuthLocalSource

@Inject
@ContributesBinding(AppScope::class, multibinding = true)
class AuthSignOutListener(
    private val authLocalSourceLazy: Lazy<AuthLocalSource>,
) : SignOutListener {

    private val authLocalSource by lazy { authLocalSourceLazy.value }

    override suspend fun onSignOut() {
        authLocalSource.clearLocalData()
    }
}

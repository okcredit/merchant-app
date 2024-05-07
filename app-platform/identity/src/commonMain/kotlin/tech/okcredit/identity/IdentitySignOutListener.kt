package tech.okcredit.identity

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener

@Inject
class IdentitySignOutListener(
    private val repository: IdentityRepository,
) : SignOutListener {

    override suspend fun onSignOut() {
        repository.clearLocalData()
    }
}

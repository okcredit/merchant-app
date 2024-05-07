package tech.okcredit.ab

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener

@Inject
class AbSignOutListener(private val repository: AbRepository) : SignOutListener {

    override suspend fun onSignOut() {
        repository.clearLocalData()
    }
}

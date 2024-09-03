package tech.okcredit.collection

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener

@Inject
class CollectionSignOutListener(
    private val repository: CollectionRepository,
) : SignOutListener {

    override suspend fun onSignOut() {
        repository.clearLocalData()
    }
}

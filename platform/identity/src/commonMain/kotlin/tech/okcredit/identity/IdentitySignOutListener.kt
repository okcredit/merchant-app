package tech.okcredit.identity

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class, multibinding = true)
class IdentitySignOutListener(
    private val repository: IdentityRepository,
) : SignOutListener {

    override suspend fun onSignOut() {
        repository.clearLocalData()
    }
}

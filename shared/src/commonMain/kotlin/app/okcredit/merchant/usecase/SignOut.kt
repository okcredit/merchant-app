package app.okcredit.merchant.usecase

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.SignOutListener

@Inject
class SignOut(
    private val signOutListeners: Set<SignOutListener>,
) {

    suspend fun execute() {
        coroutineScope {
            val deferredListeners = mutableListOf<Deferred<*>>()
            signOutListeners.forEach {
                deferredListeners.add(async { it.onSignOut() })
            }
            deferredListeners.awaitAll()
        }
    }
}

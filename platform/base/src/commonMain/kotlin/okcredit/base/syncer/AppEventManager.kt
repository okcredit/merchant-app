package okcredit.base.syncer

import me.tatarka.inject.annotations.Inject

@Inject
class AppEventManager(
    private val appOpenListeners: Set<AppEventListener<AppEvent.AppOpenEvent>>,
    private val appCreateListeners: Set<AppEventListener<AppEvent.AppCreateEvent>>,
    private val periodicListeners: Set<AppEventListener<AppEvent.Periodic24HourSyncEvent>>,
    private val signOutListeners: Set<AppEventListener<AppEvent.SignOutEvent>>,
) {

    fun onEvent(event: AppEvent) {
        when (event) {
            AppEvent.AppCreateEvent -> {
                appCreateListeners.forEach { it.execute(event as AppEvent.AppCreateEvent) }
            }
            AppEvent.AppOpenEvent -> {
                appOpenListeners.forEach { it.execute(event as AppEvent.AppOpenEvent) }
            }
            AppEvent.Periodic24HourSyncEvent -> {
                periodicListeners.forEach { it.execute(event as AppEvent.Periodic24HourSyncEvent) }
            }
            AppEvent.SignOutEvent -> {
                signOutListeners.forEach { it.execute(event as AppEvent.SignOutEvent) }
            }
        }
    }
}

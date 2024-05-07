package okcredit.base.syncer

sealed class AppEvent {
    object AppCreateEvent : AppEvent()
    object AppOpenEvent : AppEvent()
    object Periodic24HourSyncEvent : AppEvent()
    object SignOutEvent : AppEvent()
}

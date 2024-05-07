package okcredit.base.syncer

interface AppEventListener<T : AppEvent> {
    fun execute(appEvent: T)
}

package tech.okcredit.ab

import me.tatarka.inject.annotations.Provides

interface DesktopAbDatabaseComponent {
    @Provides
    fun DesktopAbDriverFactory.bind(): AbDriverFactory = this
}

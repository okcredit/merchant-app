package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides

interface DesktopOkDocComponent {

    @Provides
    fun binds(driver: DesktopOkDocDriverFactory): OkDocDriverFactory {
        return driver
    }
}

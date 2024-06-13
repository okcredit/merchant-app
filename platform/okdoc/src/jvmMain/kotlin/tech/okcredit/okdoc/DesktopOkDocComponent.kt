package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import tech.okcredit.okdoc.di.OkDocDriverFactory

interface DesktopOkDocComponent {

    @Provides
    fun binds(driver: DesktopOkDocDriverFactory): OkDocDriverFactory {
        return driver
    }
}

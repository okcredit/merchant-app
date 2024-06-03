package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import tech.okcredit.okdoc.di.OkDocDriverFactory

interface IosOkDocComponent {

    @Provides
    fun binds(driver: IosOkDocDriverFactory): OkDocDriverFactory {
        return driver
    }
}

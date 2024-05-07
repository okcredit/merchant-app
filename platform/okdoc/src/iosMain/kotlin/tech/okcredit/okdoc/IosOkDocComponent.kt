package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides

interface IosOkDocComponent {

    @Provides
    fun binds(driver: IosOkDocDriverFactory): OkDocDriverFactory {
        return driver
    }
}

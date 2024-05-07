package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import tech.okcredit.okdoc.di.OkDocComponent

interface AndroidOkDocComponent : OkDocComponent {

    @Provides
    fun binds(driver: AndroidOkDocDriverFactory): OkDocDriverFactory {
        return driver
    }

    @Provides
    fun okdocSyncer(syncer: AndroidOkDocSyncer): OkDocSyncer {
        return syncer
    }
}

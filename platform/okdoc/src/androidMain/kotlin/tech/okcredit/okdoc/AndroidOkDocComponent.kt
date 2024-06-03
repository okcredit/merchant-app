package tech.okcredit.okdoc

import me.tatarka.inject.annotations.Provides
import tech.okcredit.okdoc.di.OkDocComponent
import tech.okcredit.okdoc.di.OkDocDriverFactory

interface AndroidOkDocComponent : OkDocComponent {

    @Provides
    fun AndroidOkDocDriverFactory.binds(): OkDocDriverFactory = this

    @Provides
    fun AndroidOkDocSyncer.binds(): OkDocSyncer = this
}

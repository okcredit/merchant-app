package tech.okcredit.okdoc

import android.content.Context
import me.tatarka.inject.annotations.Provides
import okcredit.base.local.AndroidSqlDriverFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.okdoc.di.OkDocComponent
import tech.okcredit.okdoc.di.OkDocDriverFactory
import tech.okcredit.okdoc.local.OkDocDatabase

@ContributesTo(AppScope::class)
interface AndroidOkDocComponent : OkDocComponent {

    @Provides
    fun provideOkDocDriverFactory(context: Context): OkDocDriverFactory {
        return AndroidSqlDriverFactory(
            context = context,
            schema = OkDocDatabase.Schema,
            name = "okcredit_okdoc.db",
        )
    }

    @Provides
    fun AndroidOkDocSyncer.binds(): OkDocSyncer = this
}

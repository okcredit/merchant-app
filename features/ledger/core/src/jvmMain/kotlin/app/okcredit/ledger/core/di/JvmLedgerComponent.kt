package app.okcredit.ledger.core.di

import me.tatarka.inject.annotations.Provides

interface JvmLedgerComponent {

    @Provides
    fun JvmLedgerDriverFactory.bind(): LedgerDriverFactory = this
}
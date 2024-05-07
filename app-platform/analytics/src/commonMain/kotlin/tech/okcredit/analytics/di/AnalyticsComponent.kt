package tech.okcredit.analytics.di

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import tech.okcredit.analytics.AnalyticEventsConsumer
import tech.okcredit.analytics.DebugEventsConsumer

interface AnalyticsComponent {

    @Provides
    @IntoSet
    fun debugEventsConsumer(debugEventsConsumer: DebugEventsConsumer): AnalyticEventsConsumer {
        return debugEventsConsumer
    }
}

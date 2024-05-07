package tech.okcredit.analytics

import me.tatarka.inject.annotations.IntoSet
import me.tatarka.inject.annotations.Provides
import tech.okcredit.analytics.di.AnalyticsComponent

interface AndroidAnalyticsComponent : AnalyticsComponent {

    @Provides
    @IntoSet
    fun mixpanelEventsConsumer(mixpanelEventsConsumer: MixpanelEventsConsumer): AnalyticEventsConsumer {
        return mixpanelEventsConsumer
    }

    @Provides
    @IntoSet
    fun firebaseEventsConsumer(firebaseEventsConsumer: FirebaseEventsConsumer): AnalyticEventsConsumer {
        return firebaseEventsConsumer
    }
}

package tech.okcredit.analytics

import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import tech.okcredit.analytics.di.AnalyticsComponent

@ContributesTo(AppScope::class)
interface AndroidAnalyticsComponent : AnalyticsComponent

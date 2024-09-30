package app.okcredit.merchant

import app.okcredit.ledger.core.di.IosLedgerComponent
import app.okcredit.ledger.core.di.LedgerComponent
import app.okcredit.ledger.ui.LedgerScreenRegistryProvider
import app.okcredit.ledger.ui.di.LedgerUiComponent
import app.okcredit.merchant.di.SharedComponent
import app.okcredit.onboarding.OnboardingComponent
import app.okcredit.onboarding.OnboardingScreenRegistryProvider
import app.okcredit.web.WebComponent
import app.okcredit.web.WebScreenRegistryProvider
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.AppScreenModelFactory
import okcredit.base.di.AppVersion
import okcredit.base.di.AppVersionCode
import okcredit.base.di.BaseUrl
import okcredit.base.di.Debug
import okcredit.base.di.Flavor
import okcredit.base.di.IosBaseComponent
import okcredit.base.di.Singleton
import tech.okcredit.ab.IosAbComponent
import tech.okcredit.analytics.di.AnalyticsComponent
import tech.okcredit.auth.IosAuthComponent
import tech.okcredit.collection.IosCollectionComponent
import tech.okcredit.customization.di.IosCustomizationComponent
import tech.okcredit.device.IosDeviceComponent
import tech.okcredit.identity.IosIdentityComponent
import tech.okcredit.okdoc.IosOkDocComponent

@Component
@Singleton
abstract class ApplicationComponent(
    @get:Provides val baseUrl: BaseUrl,
    @get:Provides val appVersion: AppVersion,
    @get:Provides val versionCode: AppVersionCode,
    @get:Provides val debug: Debug,
    @get:Provides val flavor: Flavor,
) : IosBaseComponent,
    AnalyticsComponent,
    IosAbComponent,
    IosAuthComponent,
    IosDeviceComponent,
    IosIdentityComponent,
    IosOkDocComponent,
    SharedComponent,
    OnboardingComponent,
    LedgerComponent,
    LedgerUiComponent,
    IosLedgerComponent,
    IosCustomizationComponent,
    IosCollectionComponent,
    WebComponent {

    companion object;

    abstract val onboardingScreenRegistryProvider: OnboardingScreenRegistryProvider

    abstract val sharedScreenRegistryProvider: SharedScreenRegistryProvider

    abstract val appScreenModelFactory: AppScreenModelFactory

    abstract val ledgerScreenRegistryProvider: LedgerScreenRegistryProvider

    abstract val webScreenRegistryProvider: WebScreenRegistryProvider
}

@KmpComponentCreate
expect fun ApplicationComponent.Companion.createKmp(
    baseUrl: BaseUrl,
    appVersion: AppVersion,
    versionCode: AppVersionCode,
    debug: Debug,
    flavor: Flavor,
): ApplicationComponent

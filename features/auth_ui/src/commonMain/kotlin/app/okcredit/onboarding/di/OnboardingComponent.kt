package app.okcredit.onboarding.di

import app.okcredit.onboarding.businessName.EnterBusinessNameScreenModel
import app.okcredit.onboarding.login.LoginScreenModel
import me.tatarka.inject.annotations.IntoMap
import me.tatarka.inject.annotations.Provides
import okcredit.base.di.ScreenModelPair

interface OnboardingComponent {

    @Provides
    @IntoMap
    fun loginScreenModel(loginScreenModel: LoginScreenModel): ScreenModelPair {
        return LoginScreenModel::class to loginScreenModel
    }

    @Provides
    @IntoMap
    fun enterBusinessNameScreenModel(screenModel: EnterBusinessNameScreenModel): ScreenModelPair {
        return EnterBusinessNameScreenModel::class to screenModel
    }
}

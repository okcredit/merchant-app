package tech.okcredit.ab

import me.tatarka.inject.annotations.Provides
import tech.okcredit.ab.di.AbComponent

interface IosAbComponent : AbComponent {
    @Provides
    fun IosAbAbDriverFactory.bind(): AbDriverFactory = this

    @Provides
    fun IosProfileSyncer.bind(): ProfileSyncer = this

    @Provides
    fun IosExperimentSyncer.bind(): ExperimentSyncer = this
}

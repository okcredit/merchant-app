package tech.okcredit.ab

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.AppInitializer
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class, multibinding = true)
class AbAppInitializer(
    private val profileSyncer: ProfileSyncer,
) : AppInitializer {
    override fun init() {
        // profileSyncer.schedule(mapOf<String, String>().toJsonObject())
    }
}

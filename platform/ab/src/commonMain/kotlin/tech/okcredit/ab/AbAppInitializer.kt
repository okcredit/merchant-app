package tech.okcredit.ab

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.AppInitializer

@Inject
class AbAppInitializer(
    private val profileSyncer: ProfileSyncer,
) : AppInitializer {
    override fun init() {
        // profileSyncer.schedule(mapOf<String, String>().toJsonObject())
    }
}

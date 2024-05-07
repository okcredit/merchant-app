package tech.okcredit.ab

import me.tatarka.inject.annotations.Inject
import okcredit.base.syncer.AppInitializer

@Inject
class AbAppInitializer(
    private val abRepository: AbRepository,
) : AppInitializer {
    override fun init() {}
}

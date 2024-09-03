package tech.okcredit.customization.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import me.tatarka.inject.annotations.Inject
import tech.okcredit.customization.local.CustomizationLocalSource
import tech.okcredit.customization.models.Component
import tech.okcredit.identity.contract.usecase.GetActiveBusinessId

@Inject
class GetCustomization(
    private val getActiveBusinessId: GetActiveBusinessId,
    private val customizationLocalSource: CustomizationLocalSource,
) {

    fun execute(target: String): Flow<List<Component>> {
        return flow {
            val businessId = getActiveBusinessId.execute()
            emitAll(
                customizationLocalSource.listComponentsForTarget(
                    businessId = businessId,
                    target = target,
                ),
            )
        }
    }
}

@file:Suppress("UNCHECKED_CAST")

package okcredit.base.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import okcredit.base.ui.BaseViewEvent
import okcredit.base.ui.CoroutineScreenModel
import kotlin.reflect.KClass

typealias ScreenModelPair = Pair<KClass<out CoroutineScreenModel<*, *, *>>, CoroutineScreenModel<*, *, *>>

@Inject
class AppScreenModelFactory(
    private val screenModelMap: Map<KClass<out CoroutineScreenModel<*, *, *>>, CoroutineScreenModel<*, *, *>>,
) {
    @Suppress("UNCHECKED_CAST")
    fun <VM> create(kClass: KClass<VM>): VM where VM : CoroutineScreenModel<*, *, *> {
        return (screenModelMap[kClass] as? VM).also { it?.start() }
            ?: error("No screen model found for $kClass")
    }
}

val LocalScreenModelFactory = compositionLocalOf { AppScreenModelFactory(emptyMap()) }

@Composable
fun <VM> Screen.rememberScreenModel(clazz: KClass<out CoroutineScreenModel<*, *, *>>): VM where VM : CoroutineScreenModel<*, *, *> {
    val screenModelFactory = LocalScreenModelFactory.current
    return rememberScreenModel { screenModelFactory.create(clazz) } as VM
}

@Composable
fun <E : BaseViewEvent> CoroutineScreenModel<*, E, *>.observeViewEvents(block: (viewEvent: E) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    viewEvents.onEach {
        block(it)
    }.launchIn(coroutineScope)
}

fun Navigator.moveTo(screen: ScreenProvider) {
    push(ScreenRegistry.get(screen))
}

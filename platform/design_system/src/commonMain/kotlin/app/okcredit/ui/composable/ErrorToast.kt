package app.okcredit.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.dokar.sonner.Toast
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState

fun ToasterState.shortToast(message: String) {
    show(Toast(message))
}

@Composable
fun ErrorToast(state: ToasterState) {
    Toaster(
        state = state,
        border = { _ ->
            BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        },
        background = { _ ->
            Brush.sweepGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                ),
            )
        },
    )
}

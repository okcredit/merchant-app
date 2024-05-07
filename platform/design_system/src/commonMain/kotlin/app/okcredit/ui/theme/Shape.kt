package app.okcredit.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val shapes =
    Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(percent = 50),
        medium = RoundedCornerShape(8.dp),
        large = RoundedCornerShape(percent = 50),
        extraLarge = RoundedCornerShape(32.dp),
    )

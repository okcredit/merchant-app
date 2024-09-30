package app.okcredit.ledger.ui.composable

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LedgerLoadingShimmerView() {
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.9f),
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutLinearInEasing,
            ),
        ),
        label = "",
    )
    val brush = Brush.linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value,
        ),
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        ShimmerDateView(
            brush,
            modifier = Modifier
                .width(120.dp)
                .height(23.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        ShimmerItem(
            brush = brush,
            modifier = Modifier
                .width(220.dp)
                .height(40.dp),
            alignment = Alignment.TopEnd,
        )
        Spacer(modifier = Modifier.height(24.dp))
        ShimmerItem(
            brush = brush,
            modifier = Modifier
                .width(220.dp)
                .height(40.dp),
            alignment = Alignment.TopStart,
        )
        Spacer(modifier = Modifier.height(24.dp))
        ShimmerItem(
            brush = brush,
            modifier = Modifier
                .width(220.dp)
                .height(40.dp),
            alignment = Alignment.TopStart,
        )
        Spacer(modifier = Modifier.height(24.dp))
        ShimmerDateView(
            brush,
            modifier = Modifier
                .width(120.dp)
                .height(23.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        ShimmerItem(
            brush = brush,
            modifier = Modifier
                .width(220.dp)
                .height(40.dp),
            alignment = Alignment.TopEnd,
        )
        Spacer(modifier = Modifier.height(24.dp))
        ShimmerItem(
            brush = brush,
            modifier = Modifier
                .width(220.dp)
                .height(40.dp),
            alignment = Alignment.TopStart,
        )
    }
}

@Composable
fun ShimmerDateView(
    brush: Brush,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = modifier
                .align(Alignment.Center)
                .background(brush = brush, shape = RoundedCornerShape(16.dp)),
        )
    }
}

@Composable
fun ShimmerItem(brush: Brush, modifier: Modifier = Modifier, alignment: Alignment) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
    ) {
        Box(
            modifier = modifier
                .align(alignment)
                .background(brush = brush, shape = RoundedCornerShape(8.dp)),
        )
    }
}

@Preview
@Composable
fun ShimmerViewPreview() {
    LedgerLoadingShimmerView()
}

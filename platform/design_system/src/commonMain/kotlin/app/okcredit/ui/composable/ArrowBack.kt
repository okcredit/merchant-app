package app.okcredit.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.okcredit.ui.icon_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArrowBack(onBackClicked: () -> Unit) {
    Icon(
        modifier = Modifier.clickable(onClick = onBackClicked),
        painter = painterResource(app.okcredit.ui.Res.drawable.icon_back),
        contentDescription = "arrow_back",
    )
}

@Preview
@Composable
fun ArrowBackPreview() {
    ArrowBack {}
}

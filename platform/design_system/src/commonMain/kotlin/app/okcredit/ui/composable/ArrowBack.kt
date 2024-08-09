package app.okcredit.ui.composable

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import app.okcredit.ui.icon_back
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ArrowBack(onBackClicked: () -> Unit) {
    Icon(
        painter = painterResource(app.okcredit.ui.Res.drawable.icon_back),
        contentDescription = "arrow_back",
    )
}

@Preview
@Composable
fun ArrowBackPreview() {
    ArrowBack {}
}

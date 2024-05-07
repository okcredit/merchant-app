package app.okcredit.merchant.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun HomeUi(state: HomeContract.State) {
    Box {
        Text(text = "this is home screen")
    }
}

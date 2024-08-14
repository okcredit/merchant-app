package app.okcredit.merchant.payment

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import app.okcredit.ui.Res
import app.okcredit.ui.icon_collections
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.painterResource

object HomePaymentTab : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                title = "Payment",
                icon = painterResource(Res.drawable.icon_collections),
                index = 1u
            )
        }

    @Composable
    override fun Content() {
        Text("Payment")
    }
}
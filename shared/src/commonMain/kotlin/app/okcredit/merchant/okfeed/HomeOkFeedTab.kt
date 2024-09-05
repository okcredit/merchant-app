package app.okcredit.merchant.okfeed

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object HomeOkFeedTab : Tab {
    override val options: TabOptions
        @Composable get() {
            return TabOptions(
                title = "Feed",
                icon = null,
                index = 3u,
            )
        }

    @Composable
    override fun Content() {
        Text("Feed")
    }
}

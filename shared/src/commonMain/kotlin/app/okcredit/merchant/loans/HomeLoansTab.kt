package app.okcredit.merchant.loans

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object HomeLoansTab : Tab {

    @Composable
    override fun Content() {
        Text("Loans")
    }


    override val options: TabOptions
        @Composable get() = TabOptions(
            title = "Loans",
            icon = null,
            index = 2u
        )

}
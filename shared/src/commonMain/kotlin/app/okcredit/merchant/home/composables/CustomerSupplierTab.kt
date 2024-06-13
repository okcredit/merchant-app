package app.okcredit.merchant.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.home.HomeTab
import app.okcredit.merchant.home.isSupplierTab
import app.okcredit.ui.theme.grey50
import app.okcredit.ui.theme.white

@Composable
fun CustomerSupplierTab(
    selectedTab: HomeTab,
    width: Dp = 224.dp,
    height: Dp = 40.dp,
    gapBetweenThumbAndTrackEdge: Dp = 4.dp,
    onTabChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.width(width = width)
            .height(height = height)
            .background(grey50, RoundedCornerShape(20.dp))
            .padding(2.dp)
    ) {
        Text(
            text = "Customer",
            modifier = Modifier
                .weight(1.0f)
                .background(
                    if (selectedTab.isSupplierTab()) white else grey50, RoundedCornerShape(50)
                )
        )

        Text(
            text = "Supplier",
            modifier = Modifier
                .weight(1.0f)
                .background(
                    if (selectedTab.isSupplierTab()) white else grey50, RoundedCornerShape(50)
                )
        )
    }
}
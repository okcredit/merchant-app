package app.okcredit.merchant.ledger.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.ledger.HomeTab
import app.okcredit.merchant.ledger.isCustomerTab
import app.okcredit.merchant.ledger.isSupplierTab

@Composable
fun CustomerSupplierTab(
    selectedTab: HomeTab,
    modifier: Modifier,
    onTabChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50))
            .padding(2.dp)
    ) {
        Surface(
            onClick = { onTabChanged(false) },
            color = if (selectedTab.isCustomerTab()) MaterialTheme.colorScheme.surface else Color.Transparent,
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1.0f).padding(2.dp).fillMaxHeight()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Customer",
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selectedTab.isCustomerTab()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Surface(
            onClick = { onTabChanged(true) },
            color = if (selectedTab.isSupplierTab()) MaterialTheme.colorScheme.surface else Color.Transparent,
            shape = RoundedCornerShape(50),
            modifier = Modifier.weight(1.0f).padding(2.dp).fillMaxHeight()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Supplier",
                    modifier = Modifier,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (selectedTab.isSupplierTab()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
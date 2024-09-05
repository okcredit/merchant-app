package app.okcredit.merchant.search.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.okcredit.merchant.search.HeaderType
import app.okcredit.merchant.search.HomeSearchItem
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.contacts
import merchant_app.shared.generated.resources.customers
import merchant_app.shared.generated.resources.suppliers
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeSearchHeaderItem(item: HomeSearchItem.HeaderItem) {
    Text(
        text = findLabelForHeader(item.type),
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelSmall,
    )
}

@Composable
fun findLabelForHeader(type: HeaderType): String {
    return when (type) {
        HeaderType.CUSTOMER -> stringResource(resource = Res.string.customers)
        HeaderType.SUPPLIER -> stringResource(resource = Res.string.suppliers)
        HeaderType.CONTACT -> stringResource(resource = Res.string.contacts)
        HeaderType.RECENT_SEARCH -> "Recent Searches"
    }
}

package app.okcredit.merchant.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.grey100
import coil3.compose.AsyncImage

@Composable
fun HomeMoreOptionsBottomSheet(
    items: List<MoreOptionItem>,
    onMoreItemClicked: (MoreOption, String?) -> Unit
) {
    OkCreditTheme {
        Card (
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                items(items) {
                    MoreOptionItemView(
                        item = it,
                        onMoreItemClicked = onMoreItemClicked
                    )
                }
            }
        }
    }
}

@Composable
fun MoreOptionItemView(item: MoreOptionItem, onMoreItemClicked: (MoreOption, String?) -> Unit) {
    Surface(
        onClick = { onMoreItemClicked(item.id, item.deeplink) },
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(grey100, shape = CircleShape)
            ) {
                AsyncImage(
                    model = item.iconUrl ?: item.icon,
                    contentDescription = item.title,
                    colorFilter = if (item.id == MoreOption.PSP_UPI) {
                        // this is intentional as we want to show the icon as it is for UPI
                        null
                    } else {
                        ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(20.dp)
                )
            }

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

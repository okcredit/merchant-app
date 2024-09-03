package app.okcredit.merchant.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.colorAlphasBlackAlpha40
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

data class MoreOptionItem(
    val id: MoreOption,
    val icon: DrawableResource?,
    val title: String,
    val deeplink: String? = null,
    val iconUrl: String? = null,
)

enum class MoreOption {
    STATEMENT,
    PROFILE,
    HELP,
    SETTINGS,
    SUBSCRIPTION,
    BILLING,
    INVENTORY,
    PSP_UPI,
    DYNAMIC, // all the options coming from side menu dynamic component
}

@Composable
fun HomeMoreOptions(
    modifier: Modifier,
    items: List<MoreOptionItem>,
    onMoreItemClicked: (MoreOption, String?) -> Unit,
) {
    OkCreditTheme {
        Box(modifier = Modifier.fillMaxSize().background(colorAlphasBlackAlpha40)) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = modifier.padding(vertical = 8.dp),
                shadowElevation = 12.dp,
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.padding(vertical = 8.dp),
                ) {
                    items(items) {
                        MoreOptionItem(
                            item = it,
                            onMoreItemClicked = onMoreItemClicked,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MoreOptionItem(item: MoreOptionItem, onMoreItemClicked: (MoreOption, String?) -> Unit) {
    Surface(
        onClick = { onMoreItemClicked(item.id, item.deeplink) },
        shape = RoundedCornerShape(8.dp),
        color = Color.Transparent,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.background, shape = CircleShape),
            ) {
                if (!item.iconUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = item.iconUrl,
                        contentDescription = item.title,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp),
                    )
                } else {
                    Image(
                        painter = painterResource(item.icon!!),
                        contentDescription = item.title,
                        colorFilter = if (item.id == MoreOption.PSP_UPI) {
                            // this is intentional as we want to show the icon as it is for UPI
                            null
                        } else {
                            ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(20.dp),
                    )
                }
            }

            Text(
                text = item.title,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                minLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

package app.okcredit.merchant.ledger.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.merchant.ledger.HomeContract
import app.okcredit.ui.theme.OkCreditTheme
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.size.Size
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DynamicComponent(
    dynamicItems: List<HomeContract.DynamicItem>,
    userAlert: HomeContract.UserAlert?,
    onDynamicItemClicked: (String, String) -> Unit,
    onUserAlertClicked: (HomeContract.UserAlert) -> Unit,
) {
    Column {
        if (userAlert != null) {
            Spacer(modifier = Modifier.height(4.dp))
            UserAlertBanner(userAlert, onUserAlertClicked)
            if (dynamicItems.isEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        if (dynamicItems.isNotEmpty()) {
            when (dynamicItems.size) {
                1 -> {
                    SingleDynamicItem(
                        item = dynamicItems.first(),
                        onDynamicItemClicked = onDynamicItemClicked,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }

                2 -> {
                    DoubleDynamicItems(
                        firstItem = dynamicItems[0],
                        secondItem = dynamicItems[1],
                        onDynamicItemClicked = onDynamicItemClicked,
                    )
                }

                else -> {
                    TripleDynamicItems(
                        firstItem = dynamicItems[0],
                        secondItem = dynamicItems[1],
                        thirdItem = dynamicItems[2],
                        onDynamicItemClicked = onDynamicItemClicked,
                    )
                }
            }
        }
    }
}

@Composable
fun TripleDynamicItems(
    firstItem: HomeContract.DynamicItem,
    secondItem: HomeContract.DynamicItem,
    thirdItem: HomeContract.DynamicItem,
    onDynamicItemClicked: (String, String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
    ) {
        SingleVerticalDynamicItem(
            item = firstItem,
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(start = 16.dp, end = 6.dp),
        )
        SingleVerticalDynamicItem(
            item = secondItem,
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 6.dp, start = 6.dp),
        )
        SingleVerticalDynamicItem(
            item = thirdItem,
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 16.dp, start = 6.dp),
        )
    }
}

@Composable
fun DoubleDynamicItems(
    firstItem: HomeContract.DynamicItem,
    secondItem: HomeContract.DynamicItem,
    onDynamicItemClicked: (String, String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        SingleDynamicItem(
            item = firstItem.copy(subtitle = null),
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 12.dp),
        )
        SingleDynamicItem(
            item = secondItem.copy(subtitle = null),
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 16.dp, start = 6.dp, top = 8.dp, bottom = 12.dp),
        )
    }
}

@Composable
fun SingleVerticalDynamicItem(
    item: HomeContract.DynamicItem,
    modifier: Modifier = Modifier,
    onDynamicItemClicked: (String, String) -> Unit,
) {
    Surface(
        onClick = {
            onDynamicItemClicked(item.deeplink, item.id)
            item.trackOnItemClicked()
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp),
        ) {
            if (item.icon.endsWith(".gif")) {
                GifImage(
                    gif = item.icon,
                    modifier = Modifier.size(40.dp),
                )
            } else {
                AsyncImage(
                    model = item.icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun GifImage(
    gif: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalPlatformContext.current
    val imageLoader = ImageLoader.Builder(context).build()
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(data = gif).apply { size(Size.ORIGINAL) }
                .build(),
            imageLoader = imageLoader,
        ),
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
fun SingleDynamicItem(
    item: HomeContract.DynamicItem,
    modifier: Modifier = Modifier,
    onDynamicItemClicked: (String, String) -> Unit,
) {
    Surface(
        onClick = {
            onDynamicItemClicked(item.deeplink, item.id)
            item.trackOnItemClicked()
        },
        shape = MaterialTheme.shapes.small,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (item.icon.endsWith(".gif")) {
                GifImage(
                    gif = item.icon,
                    modifier = Modifier.size(40.dp),
                )
            } else {
                AsyncImage(
                    model = item.icon,
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 8.dp),
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!item.subtitle.isNullOrEmpty()) {
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun TripleDynamicItemsPreview() {
    OkCreditTheme {
        TripleDynamicItems(
            firstItem = HomeContract.DynamicItem(
                id = "1",
                icon = "",
                title = "GST Bills",
                subtitle = "subtitle",
                deeplink = "",
            ),
            secondItem = HomeContract.DynamicItem(
                id = "2",
                icon = "",
                title = "Stock Inventory",
                subtitle = "subtitle",
                deeplink = "",
            ),
            thirdItem = HomeContract.DynamicItem(
                id = "3",
                icon = "",
                title = "Stock Inventory",
                subtitle = "subtitle",
                deeplink = "",
            ),
            onDynamicItemClicked = { _, _ -> },
        )
    }
}

@Preview
@Composable
fun DoubleDynamicItemsPreview() {
    OkCreditTheme {
        DoubleDynamicItems(
            firstItem = HomeContract.DynamicItem(
                id = "1",
                icon = "",
                title = "GST Bills",
                subtitle = "subtitle",
                deeplink = "",
            ),
            secondItem = HomeContract.DynamicItem(
                id = "2",
                icon = "",
                title = "Stock Inventory",
                subtitle = "subtitle",
                deeplink = "",
            ),
            onDynamicItemClicked = { _, _ -> },
        )
    }
}

@Preview
@Composable
fun SingleDynamicItemPreview() {
    OkCreditTheme {
        SingleDynamicItem(
            item = HomeContract.DynamicItem(
                id = "1",
                icon = "",
                title = "title",
                subtitle = "subtitle",
                deeplink = "",
            ),
            onDynamicItemClicked = { _, _ -> },
        )
    }
}

package app.okcredit.merchant.home.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.merchant.home.HomeContract
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.grey100
import app.okcredit.ui.theme.grey200
import coil3.compose.AsyncImage
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
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                2 -> {
                    DoubleDynamicItems(
                        firstItem = dynamicItems[0],
                        secondItem = dynamicItems[1],
                        onDynamicItemClicked = onDynamicItemClicked
                    )
                }

                else -> {
                    TripleDynamicItems(
                        firstItem = dynamicItems[0],
                        secondItem = dynamicItems[1],
                        thirdItem = dynamicItems[2],
                        onDynamicItemClicked = onDynamicItemClicked
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
                .padding(start = 16.dp, end = 6.dp)
        )
        SingleVerticalDynamicItem(
            item = secondItem,
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 6.dp, start = 6.dp)
        )
        SingleVerticalDynamicItem(
            item = thirdItem,
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 16.dp, start = 6.dp)
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
                .padding(start = 16.dp, end = 6.dp, top = 8.dp, bottom = 12.dp)
        )
        SingleDynamicItem(
            item = secondItem.copy(subtitle = null),
            onDynamicItemClicked = onDynamicItemClicked,
            modifier = Modifier
                .weight(1.0f)
                .padding(end = 16.dp, start = 6.dp, top = 8.dp, bottom = 12.dp)
        )
    }
}

@Composable
fun SingleVerticalDynamicItem(
    item: HomeContract.DynamicItem,
    modifier: Modifier = Modifier,
    onDynamicItemClicked: (String, String) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable {
                onDynamicItemClicked(item.deeplink, item.id)
                item.trackOnItemClicked()
            }
            .semantics {
                contentDescription = item.title
            },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
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
                    placeholder = remember {
                        ColorPainter(color = grey200)
                    },
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
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun GifImage(
    gif: String,
    modifier: Modifier = Modifier,
) {

}

@Composable
fun SingleDynamicItem(
    item: HomeContract.DynamicItem,
    modifier: Modifier = Modifier,
    onDynamicItemClicked: (String, String) -> Unit,
) {
    Card(
        modifier = modifier
            .clickable {
                onDynamicItemClicked(item.deeplink, item.id)
                item.trackOnItemClicked()
            }
            .semantics {
                contentDescription = item.title
            },
        colors = CardDefaults.cardColors().copy(
            containerColor = grey100
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
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
                    placeholder = remember {
                        ColorPainter(color = grey200)
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1.0f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (!item.subtitle.isNullOrEmpty()) {
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.labelMedium
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
                deeplink = ""
            ),
            secondItem = HomeContract.DynamicItem(
                id = "2",
                icon = "",
                title = "Stock Inventory",
                subtitle = "subtitle",
                deeplink = ""
            ),
            thirdItem = HomeContract.DynamicItem(
                id = "3",
                icon = "",
                title = "Stock Inventory",
                subtitle = "subtitle",
                deeplink = ""
            ),
            onDynamicItemClicked = { _, _ -> }
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
                deeplink = ""
            ),
            secondItem = HomeContract.DynamicItem(
                id = "2",
                icon = "",
                title = "Stock Inventory",
                subtitle = "subtitle",
                deeplink = ""
            ),
            onDynamicItemClicked = { _, _ -> }
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
                deeplink = ""
            ),
            onDynamicItemClicked = { _, _ -> }
        )
    }
}

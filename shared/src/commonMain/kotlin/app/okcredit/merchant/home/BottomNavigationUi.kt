package app.okcredit.merchant.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import app.okcredit.ui.Res
import app.okcredit.ui.icon_home
import app.okcredit.ui.theme.OkCreditTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigationUi(
    list: List<BottomMenuItem>,
    selectedItem: NavItem,
    onItemClicked: (NavItem) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        shadowElevation = 12.dp,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            list.forEach { bottomMenuItem ->
                SelectableTab(
                    bottomMenuItem = bottomMenuItem,
                    modifier = Modifier.weight(1.0f),
                    selected = bottomMenuItem.navItem == selectedItem,
                    onItemClicked = onItemClicked,
                )
            }
        }
    }
}

@Composable
fun SelectableTab(
    bottomMenuItem: BottomMenuItem,
    selected: Boolean,
    modifier: Modifier,
    onItemClicked: (NavItem) -> Unit,
) {
    Box(
        modifier = modifier.clickable(onClick = { onItemClicked(bottomMenuItem.navItem) }),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(12.dp))
            val icon = if (selected && bottomMenuItem.selectedIcon != null) {
                bottomMenuItem.selectedIcon
            } else {
                bottomMenuItem.drawableId
            }
            SelectableIcon(
                selected = selected,
                icon = icon,
                showDotBadge = bottomMenuItem.showDotBadge
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = bottomMenuItem.label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
fun SelectableIcon(selected: Boolean, icon: Painter, showDotBadge: Boolean) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(24.dp)
            .background(
                color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                shape = RoundedCornerShape(50)
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = icon,
            contentDescription = "",
            tint = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )

        if (showDotBadge && !selected) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.error, shape = CircleShape)
                    .align(Alignment.TopEnd)
                    .size(4.dp)
            )
        }
    }
}

@Preview
@Composable
fun SelectableIconPreview() {
    OkCreditTheme {
        SelectableIcon(
            selected = true,
            icon = painterResource(Res.drawable.icon_home),
            showDotBadge = true
        )
    }
}

@Preview
@Composable
fun BottomNavigationUiPreview() {
    BottomNavigationUi(
        list = listOf(
            BottomMenuItem(
                navItem = NavItem.HOME_LEDGER,
                drawableId = painterResource(Res.drawable.icon_home),
                label = "Home",
            ),
            BottomMenuItem(
                navItem = NavItem.HOME_PAYMENT,
                drawableId = painterResource(Res.drawable.icon_home),
                label = "Home",
            ),
            BottomMenuItem(
                navItem = NavItem.HOME_OK_FEED,
                drawableId = painterResource(Res.drawable.icon_home),
                label = "Home",
            ),
            BottomMenuItem(
                navItem = NavItem.HOME_MORE_OPTIONS,
                drawableId = painterResource(Res.drawable.icon_home),
                label = "Home",
            )
        ),
        selectedItem = NavItem.HOME_LEDGER,
        onItemClicked = {}
    )
}

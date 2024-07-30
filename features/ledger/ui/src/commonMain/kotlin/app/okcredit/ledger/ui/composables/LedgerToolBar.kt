package app.okcredit.ledger.ui.composables

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.model.MenuOptions
import app.okcredit.ui.composable.ArrowBack
import app.okcredit.ui.composable.AvatarWithName
import app.okcredit.ui.icon_help_outline
import app.okcredit.ui.icon_menu
import app.okcredit.ui.icon_okc
import app.okcredit.ui.theme.green_primary
import app.okcredit.ui.theme.grey800
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


data class RelationshipToolBarState(
    val id: String,
    val name: String,
    val profileImage: String?,
    val mobile: String,
    val canShowContextualHelp: Boolean,
    val isDefaulter: Boolean,
    val toolbarOptions: List<MenuOptions>,
    val moreMenuOptions: List<MenuOptions>,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerToolBar(
    state: RelationshipToolBarState?,
    registered: Boolean,
    blocked: Boolean,
    onProfileClicked: (String) -> Unit,
    openMoreBottomSheet: (Boolean) -> Unit,
    onMenuOptionClicked: (MenuOptions) -> Unit,
    onBackClicked: () -> Unit,
) {
    if (state == null) {
        LoadingShimmerForToolBar(onBackClicked)
    } else {
        val toolBarOptions = if (state.toolbarOptions.size >= 3) {
            state.toolbarOptions.subList(0, 3)
        } else {
            state.toolbarOptions
        }
        Column {
            TopAppBar(
                modifier = Modifier,
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ArrowBack(onBackClicked)
                        if (state.toolbarOptions.size < 3) {
                            Spacer(modifier = Modifier.size(8.dp))
                            AvatarWithName(
                                commonLedger = false,
                                name = state.name.takeIf { it.isNotEmpty() }
                                    ?: state.mobile.takeIf { it.isNotEmpty() } ?: "0",
                                profileImage = state.profileImage,
                                verified = false,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clickable {
                                        if (blocked) {
                                            onProfileClicked(state.id)
                                        }
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Row(
                            modifier = Modifier.weight(3f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column(
                                modifier = Modifier
                                    .clickable {
                                        if (blocked) {
                                            onProfileClicked(
                                                state.id
                                            )
                                        }
                                    }
                                    .padding(vertical = 8.dp),
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = state.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Start,
                                    //style = subtitle1
                                )
                                Text(
                                    modifier = Modifier,
                                    text = "View Profile",
                                    textAlign = TextAlign.Start,
                                    //style = caption2,
                                    color = green_primary
                                )
                            }
                            if (registered) {
                                Spacer(modifier = Modifier.size(6.dp))
                                Image(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_okc),
                                    contentDescription = "okcredit_logo"
                                )
                            }
                            Spacer(modifier = Modifier.size(12.dp))
                        }
                    }
                },
                actions = {
                    if (toolBarOptions.isNotEmpty()) {
                        toolBarOptions.forEach {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        if (!blocked) {
                                            onMenuOptionClicked(it)
                                        }
                                    },
                                painter = painterResource(it.icon),
                                contentDescription = it.name,
                                tint = grey800
                            )
                            Spacer(modifier = Modifier.size(16.dp))
                        }

                        if (toolBarOptions.size > 2) {
                            Icon(
                                painter = painterResource(app.okcredit.ui.Res.drawable.icon_menu),
                                contentDescription = "menu_options",
                                modifier = Modifier
                                    .rotate(90f)
                                    .clickable {
                                        if (blocked) openMoreBottomSheet(true)
                                    },
                                tint = grey800
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                        }
                    } else {
                        Icon(
                            painter = painterResource(app.okcredit.ui.Res.drawable.icon_help_outline),
                            contentDescription = "help",
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .clickable {
                                    onMenuOptionClicked(MenuOptions.Help)
                                },
                            tint = grey800
                        )
                    }
                }
            )
        }
    }
}

@Composable
fun LoadingShimmerForToolBar(onBackClick: () -> Unit) {
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition(label = "")

    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutLinearInEasing
            )
        ),
        label = "",
    )
    val brush = Brush.linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 112.dp)
            .background(color = Color.White)
    ) {
        ShimmerGridItem(brush, onBackClick = onBackClick)
    }
}

@Composable
fun ShimmerGridItem(brush: Brush, onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ArrowBack(onBackClick)
        Spacer(modifier = Modifier.size(8.dp))
        Spacer(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1.0f)
        ) {
            Spacer(
                modifier = Modifier
                    .height(16.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .fillMaxWidth(fraction = 0.5f)
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(4.dp)) // creates an empty space between
            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .fillMaxWidth(fraction = 0.7f)
                    .background(brush)
            )
        }
    }
}


@Preview
@Composable
fun CustomerLedgerToolBarPreview() {
    val state = RelationshipToolBarState(
        toolbarOptions = listOf(
            MenuOptions.QrCode,
            MenuOptions.RelationshipStatements,
            MenuOptions.Call,
        ),
        canShowContextualHelp = false,
        moreMenuOptions = listOf(
            MenuOptions.RelationshipStatements,
            MenuOptions.QrCode,
            MenuOptions.Help
        ),
        isDefaulter = false,
        name = "John Doe",
        profileImage = null,
        id = "customerI12",
        mobile = ""
    )
    LedgerToolBar(
        state = state,

        onProfileClicked = {},
        onBackClicked = {},
        registered = true,
        blocked = true,
        openMoreBottomSheet = {},
        onMenuOptionClicked = {},
    )
}

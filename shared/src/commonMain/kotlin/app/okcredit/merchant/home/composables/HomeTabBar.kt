package app.okcredit.merchant.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.okcredit.merchant.home.HomeContract
import app.okcredit.ui.icon_namsthe
import app.okcredit.ui.icon_qr_code
import app.okcredit.ui.icon_upi
import app.okcredit.ui.icon_whatsapp
import app.okcredit.ui.theme.OkCreditTheme
import app.okcredit.ui.theme.grey100
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.need_help
import merchant_app.shared.generated.resources.share
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tech.okcredit.identity.contract.model.Business

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeTabBar(
    toolbarAction: HomeContract.ToolbarAction?,
    activeBusiness: Business?,
    primaryVpa: String?,
    onAvatarClicked: () -> Unit,
    onToolbarActionClicked: (HomeContract.ToolbarAction) -> Unit,
    onPrimaryVpaClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AvatarWithName(
            customerName = activeBusiness?.name?.takeIf { it.isNotEmpty() }
                ?: activeBusiness?.mobile?.takeIf { it.isNotEmpty() }
                ?: "O",
            profileImage = activeBusiness?.profileImage,
            commonLedger = true,
            modifier = Modifier
                .clickable { onAvatarClicked() }
                .size(40.dp)
        )
        Spacer(modifier = Modifier.weight(1.0f))
        if (!primaryVpa.isNullOrEmpty()) {
            PrimaryVpa(primaryVpa = primaryVpa, onPrimaryVpaClicked = onPrimaryVpaClicked)
            if (toolbarAction != null) {
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clickable { onToolbarActionClicked(toolbarAction) }
                        .size(40.dp)
                        .background(color = grey100, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painterResource(resource = getIconForToolbar(toolbarAction)),
                        contentDescription = "",
                    )
                }
            }
        } else {
            if (toolbarAction != null) {
                Surface(
                    onClick = {
                        onToolbarActionClicked(toolbarAction)
                    },
                    shape = RoundedCornerShape(50),
                    color = grey100,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(resource = getIconForToolbar(toolbarAction = toolbarAction)),
                            contentDescription = getLabelForToolbar(toolbarAction = toolbarAction),
                            modifier = Modifier.size(20.dp),
                            colorFilter = if (toolbarAction == HomeContract.ToolbarAction.ACTIVATE_UPI) null else ColorFilter.tint(MaterialTheme.colors.onBackground)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = getLabelForToolbar(toolbarAction = toolbarAction),
                            style = MaterialTheme.typography.subtitle2.copy(fontSize = 12.sp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun getIconForToolbar(toolbarAction: HomeContract.ToolbarAction): DrawableResource {
    return when (toolbarAction) {
        HomeContract.ToolbarAction.ACTIVATE_UPI -> app.okcredit.ui.Res.drawable.icon_upi
        HomeContract.ToolbarAction.NEED_HELP -> app.okcredit.ui.Res.drawable.icon_namsthe
        HomeContract.ToolbarAction.SHARE -> app.okcredit.ui.Res.drawable.icon_whatsapp
    }
}

@Composable
fun getLabelForToolbar(toolbarAction: HomeContract.ToolbarAction): String {
    return when (toolbarAction) {
        HomeContract.ToolbarAction.ACTIVATE_UPI -> "Activate UPI"
        HomeContract.ToolbarAction.NEED_HELP -> stringResource(resource = Res.string.need_help)
        HomeContract.ToolbarAction.SHARE -> stringResource(resource = Res.string.share)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PrimaryVpa(primaryVpa: String, onPrimaryVpaClicked: () -> Unit) {
    Surface(
        onClick = onPrimaryVpaClicked,
        shape = RoundedCornerShape(50),
        color = grey100,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Image(
                painter = painterResource(resource = app.okcredit.ui.Res.drawable.icon_qr_code),
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("UPI ID:\n")
                    }
                    append(primaryVpa)
                },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.widthIn(max = 150.dp),
                style = MaterialTheme.typography.caption,
            )
        }
    }
}

@Composable
@Preview
fun HomeTabBarPreview() {
    OkCreditTheme {
        HomeTabBar(
            toolbarAction = HomeContract.ToolbarAction.ACTIVATE_UPI,
            activeBusiness = null,
            primaryVpa = "",
            onAvatarClicked = {},
            onToolbarActionClicked = { },
            onPrimaryVpaClicked = { }
        )
    }
}

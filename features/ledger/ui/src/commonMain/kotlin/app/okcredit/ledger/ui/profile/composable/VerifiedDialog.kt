package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.got_it
import app.okcredit.ledger.ui.verified_user
import app.okcredit.ledger.ui.verified_user_caption
import app.okcredit.ui.icon_account_125dp
import app.okcredit.ui.icon_thumb_up
import app.okcredit.ui.icon_verified_user
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun VerifiedDialog(
    modifier: Modifier,
    profileImage: String,
    onPrimaryCtaClicked: () -> Unit,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .width(260.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            if (profileImage.isNotEmpty()) {
                AsyncImage(
                    model = profileImage,
                    placeholder = painterResource(app.okcredit.ui.Res.drawable.icon_account_125dp),
                    error = painterResource(app.okcredit.ui.Res.drawable.icon_account_125dp),
                    contentDescription = "profile_image",
                    modifier = Modifier
                        .size(110.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                )
            } else {
                Image(
                    painter = painterResource(app.okcredit.ui.Res.drawable.icon_account_125dp),
                    contentDescription = "profile_image",
                    modifier = Modifier
                        .size(110.dp)
                        .padding(16.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                )
            }
            Image(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_verified_user),
                contentDescription = "camera",
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .size(24.dp)
                    .align(Alignment.BottomEnd)
            )
        }
        Text(
            text = stringResource(Res.string.verified_user),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
        )
        Text(
            text = stringResource(Res.string.verified_user_caption),
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .width(260.dp)
                .padding(horizontal = 24.dp, vertical = 8.dp),
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .clickable { onPrimaryCtaClicked() },
        ) {
            Icon(
                painter = painterResource(app.okcredit.ui.Res.drawable.icon_thumb_up),
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.surface,
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(Res.string.got_it),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.surface,
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview
@Composable
fun VerifiedDialogPreview() {
    VerifiedDialog(
        modifier = Modifier,
        profileImage = "",
        onPrimaryCtaClicked = {}
    )
}

package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.okcredit.ui.Res
import app.okcredit.ui.icon_account
import app.okcredit.ui.icon_account_125dp
import app.okcredit.ui.icon_camera
import app.okcredit.ui.icon_camera_alt
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileHeader(
    profileImgUrl: String,
    onProfileImageClicked: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            if (profileImgUrl.isEmpty()) {
                Image(
                    painter = painterResource(Res.drawable.icon_account_125dp),
                    contentDescription = "profile_image",
                    modifier = Modifier
                        .size(82.dp)
                        .clickable { onProfileImageClicked() }
                        .background(color = Color.White, shape = RoundedCornerShape(20.dp))
                )
            } else {
                AsyncImage(
                    model = profileImgUrl,
                    placeholder = painterResource(Res.drawable.icon_account),
                    error = painterResource(Res.drawable.icon_account),
                    contentDescription = "profile_image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(82.dp)
                        .clickable { onProfileImageClicked() }
                        .clip(CircleShape)
                        .background(color = Color.White, shape = CircleShape)
                )
            }
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, start = 12.dp)
                    .size(24.dp)
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(20.dp))
                    .clickable { onProfileImageClicked() }
                    .align(Alignment.BottomEnd),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.icon_camera_alt),
                    contentDescription = "camera",
                    tint = Color.White,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileHeaderPreview() {
    ProfileHeader(profileImgUrl = "", onProfileImageClicked = {})
}

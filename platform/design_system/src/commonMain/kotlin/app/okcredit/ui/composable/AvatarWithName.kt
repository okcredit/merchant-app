package app.okcredit.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.okcredit.ui.Res
import app.okcredit.ui.icon_okc
import app.okcredit.ui.icon_verified_user
import app.okcredit.ui.theme.orange_primary
import app.okcredit.ui.theme.white
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs

@Composable
fun AvatarWithName(
    modifier: Modifier,
    name: String,
    profileImage: String?,
    commonLedger: Boolean,
    verified: Boolean,
) {
    if (profileImage.isNullOrEmpty()) {
        Box(
            modifier = Modifier.background(
                if (verified) orange_primary else Color.Transparent,
                shape = CircleShape
            )
        ) {
            DefaultAvatar(
                modifier = modifier.padding(if (verified) 1.dp else 0.dp),
                customerName = name.ifBlank { "U" },
                commonLedger = commonLedger,
                verified = verified,
            )
        }
    } else {
        Box(
            modifier = modifier
                .background(
                    if (verified) orange_primary else Color.Transparent,
                    shape = CircleShape
                )
        ) {
            AsyncImage(
                model = profileImage,
                contentDescription = null,
                placeholder = painterResource(Res.drawable.icon_verified_user),
                error = painterResource(Res.drawable.icon_verified_user),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .align(Alignment.Center)
                    .padding(if (verified) 1.dp else 0.dp),
            )
            if (verified) {
                VerifiedLedgerTag(modifier = Modifier.align(Alignment.BottomStart))
            } else if (commonLedger) {
                CommonLedgerTag(modifier = Modifier.align(Alignment.BottomEnd))
            }
        }
    }
}

@Composable
fun VerifiedLedgerTag(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 2.dp, end = 2.dp)
            .background(white, CircleShape)
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_verified_user),
            contentDescription = "verified",
            modifier = Modifier
                .size(18.dp)
                .padding(0.5.dp),
            tint = orange_primary
        )
    }
}

@Composable
fun DefaultAvatar(
    modifier: Modifier,
    customerName: String,
    verified: Boolean = false,
    commonLedger: Boolean = false,
) {
    Box(
        modifier = modifier
            .background(backgroundColor(customerName), CircleShape)
    ) {
        Text(
            text = customerName.first().toString().uppercase(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 9.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary,
        )

        if (verified) {
            VerifiedLedgerTag(modifier = Modifier.align(Alignment.BottomStart))
        } else if (commonLedger) {
            CommonLedgerTag(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}

@Composable
fun CommonLedgerTag(modifier: Modifier) {
    Image(
        painter = painterResource(Res.drawable.icon_okc),
        contentDescription = "common_ledger",
        modifier = modifier
            .size(20.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
    )
}


@Preview
@Composable
fun AvatarWithNamePreview() {
    AvatarWithName(
        name = "Umesh",
        profileImage = null,
        commonLedger = false,
        modifier = Modifier.size(40.dp),
        verified = true,
    )
}

val materialColors = listOf(
    Color(0xffe57373),
    Color(0xfff06292),
    Color(0xffba68c8),
    Color(0xff9575cd),
    Color(0xff7986cb),
    Color(0xff64b5f6),
    Color(0xff4fc3f7),
    Color(0xff4dd0e1),
    Color(0xff4db6ac),
    Color(0xff81c784),
    Color(0xffaed581),
    Color(0xffff8a65),
    Color(0xffd4e157),
    Color(0xffffd54f),
    Color(0xffffb74d),
    Color(0xffa1887f),
    Color(0xff90a4ae),
)

fun backgroundColor(text: String): Color {
    return materialColors[abs(text.hashCode()) % materialColors.size]
}

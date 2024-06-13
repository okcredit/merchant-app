package app.okcredit.merchant.home.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import merchant_app.shared.generated.resources.Res
import merchant_app.shared.generated.resources.defaulter_badge_red
import merchant_app.shared.generated.resources.ic_ok_credit_tag
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.abs

@Composable
fun AvatarWithName(
    customerName: String,
    profileImage: String?,
    commonLedger: Boolean,
    modifier: Modifier,
    defaulter: Boolean = false,
) {
    DefaultAvatar(
        modifier = modifier,
        customerName = customerName,
        defaulter = defaulter,
        commonLedger = commonLedger,
    )
}

@Composable
fun DefaultAvatar(
    modifier: Modifier,
    customerName: String,
    defaulter: Boolean = false,
    commonLedger: Boolean = false,
) {
    Box(
        modifier = modifier.background(backgroundColor(customerName), CircleShape)
    ) {
        Text(
            text = customerName.first().toString().uppercase(),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 9.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
        )
        if (defaulter) {
            DefaulterTag(modifier = Modifier.align(Alignment.BottomCenter))
        } else if (commonLedger) {
            CommonLedgerTag(modifier = Modifier.align(Alignment.BottomEnd))
        }
    }
}

@Composable
fun CommonLedgerTag(modifier: Modifier) {
    Image(
        painter = painterResource(resource = Res.drawable.ic_ok_credit_tag),
        contentDescription = "",
        modifier = modifier
            .size(20.dp)
            .background(MaterialTheme.colors.primary, CircleShape)
    )
}

@Composable
fun DefaulterTag(modifier: Modifier) {
    Image(
        painter = painterResource(resource = Res.drawable.defaulter_badge_red),
        contentDescription = "",
        modifier = modifier.height(12.dp)
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

package app.okcredit.onboarding.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import app.okcredit.ui.icon_recovery_mobile
import app.okcredit.ui.icon_sms_outline
import app.okcredit.ui.icon_whatsapp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.seconds

@Composable
fun EnterOtpUi(
    state: LoginContract.State,
    otpEntered: (String) -> Unit,
    resendOtpOnSms: () -> Unit,
    resendOtpOnWhatsApp: () -> Unit,
    resendOtpOnRecoveryNumber: () -> Unit,
) {
    var ticks by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (ticks < 10) {
            delay(1.seconds)
            ticks++
        }
    }
    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.surface).fillMaxSize()) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
                .size(36.dp)
                .border(
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                ),
        ) {
            Text(
                text = "${10 - ticks}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = "Enter 6 digit OTP sent to ${state.mobile}",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp),
        )
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
        ) {
            val inputValue = remember { mutableStateOf("") }
            OtpTextField(
                otpText = inputValue.value,
                onOtpTextChange = { otp, complete ->
                    inputValue.value = (otp)
                    if (complete) {
                        otpEntered.invoke(otp)
                    }
                },
            )
            if (inputValue.value.length == 6) {
                Spacer(Modifier.width(8.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically),
                    strokeWidth = 3.dp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        if (!state.error.isNullOrEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(
                text = state.error,
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 16.dp, end = 16.dp),
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        if (state.showResendOptions && ticks == 10) {
            FallbackOptionView(
                icon = app.okcredit.ui.Res.drawable.icon_sms_outline,
                title = "Send OTP via SMS",
                onClick = resendOtpOnSms,
            )
            FallbackOptionView(
                icon = app.okcredit.ui.Res.drawable.icon_whatsapp,
                title = "Send OTP via Whatsapp",
                onClick = resendOtpOnWhatsApp,
            )
            if (state.showRecoveryNumber) {
                FallbackOptionView(
                    icon = app.okcredit.ui.Res.drawable.icon_recovery_mobile,
                    title = "SMS on Recovery Mobile (${state.maskedRecoveryMobile})",
                    onClick = resendOtpOnRecoveryNumber,
                )
            }
        }
    }
}

@Composable
fun FallbackOptionView(
    icon: DrawableResource,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .clickable { onClick.invoke() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically),
            tint = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 12.dp)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1.0f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

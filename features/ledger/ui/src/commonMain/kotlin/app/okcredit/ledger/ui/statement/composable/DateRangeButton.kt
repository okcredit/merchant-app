package app.okcredit.ledger.ui.statement.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.statement.AccountStatementContract
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
fun DateRangePicker(
    fabOffsetHeightPx: MutableState<Float>,
    onClick: () -> Unit,
    state: AccountStatementContract.State,
) {
    val dateTitle = if (state.startDate.relativeDate() == state.endDate.relativeDate()) {
        state.startDate.relativeDate()
    } else {
        "${state.startDate.relativeDate()} - ${state.endDate.relativeDate()}"
    }

    Box {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset {
                        IntOffset(x = 0, y = fabOffsetHeightPx.value.roundToInt())
                    }
                    .padding(0.dp, 8.dp, 0.dp, 0.dp),
            ) {
                Card(
                    border = BorderStroke(color = MaterialTheme.colorScheme.outlineVariant, width = 1.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                ) {
                    Surface(
                        modifier = Modifier
                            .clickable(
                                onClick = { onClick.invoke() }
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = dateTitle,
                                style = MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onErrorContainer)
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ChipPreview() {
    DateRangePicker(
        fabOffsetHeightPx = remember { mutableStateOf(0f) },
        onClick = {},
        state = AccountStatementContract.State()
    )
}

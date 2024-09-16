package app.okcredit.ledger.ui.profile.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.okcredit.ledger.ui.Res
import app.okcredit.ledger.ui.profile
import app.okcredit.ui.composable.ArrowBack
import app.okcredit.ui.icon_help_outline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountProfileToolbar(
    onBackClicked: () -> Unit,
    onHelpClicked: () -> Unit,
) {
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(0.dp)
    ) {
        TopAppBar(
            modifier = Modifier,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            navigationIcon = {
                ArrowBack(onBackClicked, modifier = Modifier.padding(horizontal = 8.dp))
            },
            title = {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = stringResource(Res.string.profile),
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                )
            },
            actions = {
                IconButton(onClick = onHelpClicked) {
                    Icon(
                        painter = painterResource(app.okcredit.ui.Res.drawable.icon_help_outline),
                        contentDescription = "help_toolbar"
                    )
                }
            }
        )
    }
}

@Preview
@Composable
fun RelationshipProfileToolbarPreview() {
    AccountProfileToolbar(
        onBackClicked = {},
        onHelpClicked = {}
    )
}

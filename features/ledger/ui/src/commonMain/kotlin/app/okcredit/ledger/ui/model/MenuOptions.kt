package app.okcredit.ledger.ui.model

import app.okcredit.ui.icon_call
import app.okcredit.ui.icon_help_outline
import app.okcredit.ui.icon_qr_code
import app.okcredit.ui.icon_statement
import org.jetbrains.compose.resources.DrawableResource

sealed class MenuOptions(val icon: DrawableResource, val name: String) {

    data object QrCode : MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_qr_code,
        name = "QR Code"
    )

    data object RelationshipStatements : MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_statement,
        name = "Customer Statements"
    )

    data object Call : MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_call,
        name = "Call"
    )

    data object Help : MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_help_outline,
        name = "Help"
    )
}
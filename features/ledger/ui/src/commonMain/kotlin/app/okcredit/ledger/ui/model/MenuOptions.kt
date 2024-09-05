package app.okcredit.ledger.ui.model

import app.okcredit.ui.icon_call
import app.okcredit.ui.icon_help_outline
import app.okcredit.ui.icon_sms
import app.okcredit.ui.icon_statement_2
import app.okcredit.ui.icon_whatsapp
import org.jetbrains.compose.resources.DrawableResource

sealed class MenuOptions(val icon: DrawableResource, val name: String) {

    data object RelationshipStatements : MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_statement_2,
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

    data object RemindWithSms: MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_sms,
        name = "SMS"
    )

    data object RemindWithWhatsapp: MenuOptions(
        icon = app.okcredit.ui.Res.drawable.icon_whatsapp,
        name = "Whatsapp"
    )
}
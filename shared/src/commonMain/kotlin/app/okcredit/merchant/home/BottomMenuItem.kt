package app.okcredit.merchant.home

import androidx.compose.ui.graphics.painter.Painter

data class BottomMenuItem(
    val navItem: NavItem,
    val drawableId: Painter,
    val label: String,
    val showDotBadge: Boolean = false,
    val selectedIcon: Painter? = null,
)

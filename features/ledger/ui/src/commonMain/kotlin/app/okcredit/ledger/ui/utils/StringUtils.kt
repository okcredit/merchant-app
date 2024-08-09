package app.okcredit.ledger.ui.utils

object StringUtils  {
    fun getShortName(name: String?) : String {
        return when {
            name.isNullOrBlank() -> ""
            name.length > 10 -> name.substring(0, 10) + "..."
            else -> name
        }
    }
}
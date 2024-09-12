package app.okcredit.ledger.core.local

import app.cash.sqldelight.ColumnAdapter
import app.okcredit.ledger.contract.model.AccountType
import app.okcredit.ledger.contract.model.AccountStatus
import app.okcredit.ledger.contract.model.Transaction.Category
import app.okcredit.ledger.contract.model.Transaction.State
import app.okcredit.ledger.contract.model.Transaction.Type
import okcredit.base.units.Paisa
import okcredit.base.units.Timestamp

object TransactionStateAdapter : ColumnAdapter<State, Long> {
    override fun decode(databaseValue: Long): State {
        return State.getTransactionState(databaseValue.toInt())
    }

    override fun encode(value: State): Long {
        return value.code.toLong()
    }
}

object TransactionCategoryAdapter : ColumnAdapter<Category, Long> {
    override fun decode(databaseValue: Long): Category {
        return Category.getTransactionCategory(databaseValue.toInt())
    }

    override fun encode(value: Category): Long {
        return value.code.toLong()
    }
}

object TransactionTypeAdapter : ColumnAdapter<Type, String> {
    override fun decode(databaseValue: String): Type {
        return Type.valueOf(databaseValue)
    }

    override fun encode(value: Type): String {
        return value.name
    }
}

object TimestampAdapter : ColumnAdapter<Timestamp, Long> {

    override fun decode(databaseValue: Long): Timestamp {
        return Timestamp(databaseValue)
    }

    override fun encode(value: Timestamp): Long {
        return value.epochMillis
    }
}

object AccountTypeAdapter : ColumnAdapter<AccountType, String> {
    override fun decode(databaseValue: String): AccountType {
        return AccountType.valueOf(databaseValue)
    }

    override fun encode(value: AccountType): String {
        return value.name
    }
}

object CommandTypeAdapter : ColumnAdapter<CommandType, String> {
    override fun decode(databaseValue: String): CommandType {
        return CommandType.valueOf(databaseValue)
    }

    override fun encode(value: CommandType): String {
        return value.name
    }
}

object AccountStatusAdapter : ColumnAdapter<AccountStatus, String> {
    override fun decode(databaseValue: String): AccountStatus {
        return AccountStatus.valueOf(databaseValue)
    }

    override fun encode(value: AccountStatus): String {
        return value.name
    }
}

object PaisaAdapter : ColumnAdapter<Paisa, Long> {
    override fun decode(databaseValue: Long): Paisa {
        return Paisa(databaseValue)
    }

    override fun encode(value: Paisa): Long {
        return value.value
    }
}

object IntAdapter : ColumnAdapter<Int, Long> {
    override fun decode(databaseValue: Long): Int {
        return databaseValue.toInt()
    }

    override fun encode(value: Int): Long {
        return value.toLong()
    }
}

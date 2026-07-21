package com.pradumcodes.moneko.screens.transaction.add

enum class TransactionType {
    EXPENSE,
    INCOME,
    LOAN
}

enum class LoanType {
    LENT,
    BORROWED,
}

data class LoanDetails(
    val loanType: LoanType = LoanType.LENT,
    val personName: String = "",
    val dueDate: Long? = null
)

data class AddTransactionScreenUiState (
    val currentBalance: Double,
    val amount: Double = 0.0,
    val category: Int? = null,
    val note: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val loanDetails: LoanDetails? = null,
)

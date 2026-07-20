package com.pradumcodes.moneko.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

data class LoanSummary(
    val toReceive: Double = 0.0,
    val toPay: Double = 0.0,
)

data class SpendingCategorySummary(
    val icon: Int?,
    val categoryName: String,
    val percentage: Float,
)

enum class TransactionType {
    INCOME,
    EXPENSE,
    LOAN
}

data class RecentTransactionUi(
    val id: String,
    val title: String,
    val timestamp: Instant,
    val amount: Double,
    val type: TransactionType,
)

data class HomeUiState(

    // Hero Card
    val balance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,

    // Summary Cards
    val todaySpending: Double = 0.0,
    val todayTransactionCount: Int = 0,

    val loanSummary: LoanSummary = LoanSummary(),

    // Recent Activity
    val recentTransactions: List<RecentTransactionUi> = emptyList(),

    // Spending Categories
    val spendingCategories: List<SpendingCategorySummary> = emptyList(),

    // Screen State
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)


// Dummy Data
@RequiresApi(Build.VERSION_CODES.O)
val transactions = listOf(
    RecentTransactionUi(
        id = "1",
        title = "Salary",
        timestamp = Instant.now(),
        amount = 1000.0,
        type = TransactionType.INCOME,
    ),
    RecentTransactionUi(
        id = "2",
        title = "Groceries",
        timestamp = Instant.now(),
        amount = 500.0,
        type = TransactionType.EXPENSE,
    ),
    RecentTransactionUi(
        id = "3",
        title = "Loan",
        timestamp = Instant.now(),
        amount = 2000.0,
        type = TransactionType.LOAN,
    ),
    RecentTransactionUi(
        id = "4",
        title = "Salary",
        timestamp = Instant.now(),
        amount = 1000.0,
        type = TransactionType.INCOME,
    ),
    RecentTransactionUi(
        id = "5",
        title = "Groceries",
        timestamp = Instant.now(),
        amount = 500.0,
        type = TransactionType.EXPENSE,
    ),
)

val categories = listOf(
    SpendingCategorySummary(
        icon = null,
        categoryName = "Food",
        percentage = 40f,
    ),
    SpendingCategorySummary(
        icon = null,
        categoryName = "Transport",
        percentage = 30f,
    ),
    SpendingCategorySummary(
        icon = null,
        categoryName = "Entertainment",
        percentage = 20f,
    ),
    SpendingCategorySummary(
        icon = null,
        categoryName = "Shopping",
        percentage = 10f,
    ),
)

@RequiresApi(Build.VERSION_CODES.O)
val homeUiState = HomeUiState(
    balance = 5000.0,
    monthlyIncome = 2000.0,
    monthlyExpense = 3000.0,
    todaySpending = 1000.0,
    todayTransactionCount = 2,
    loanSummary = LoanSummary(),
    recentTransactions = transactions,
    spendingCategories = categories,
)
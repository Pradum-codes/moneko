package com.pradumcodes.moneko.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pradumcodes.moneko.screens.home.components.AppHeader
import com.pradumcodes.moneko.screens.home.components.CategorySpendingSummary
import com.pradumcodes.moneko.screens.home.components.CreditCardHero
import com.pradumcodes.moneko.screens.home.components.QuickActionsGrid
import com.pradumcodes.moneko.screens.home.components.QuickStatsGrid
import com.pradumcodes.moneko.screens.home.components.RecentActivitySection
import com.pradumcodes.moneko.ui.theme.MonekoTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val uiState = homeUiState
    // Future: Inject viewModel to collect live data
    // val viewModel: HomeViewModel = hiltViewModel()
    // val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        // Level 1: App Header
        item {
            AppHeader(userName = "Pradum Kumar")
        }

        // Level 2: Credit Card Hero Widget
        item {
            CreditCardHero(
                balance = uiState.balance,
                monthlyIncome = uiState.monthlyIncome,
                monthlyExpense = uiState.monthlyExpense
            )
        }

        // Quick Actions Grid
        item {
            QuickActionsGrid(
                onAddExpenseClick = { /* TODO: Navigate to expense entry */ },
                onAddIncomeClick = { /* TODO: Navigate to income entry */ },
                onAddLoanClick = { /* TODO: Navigate to loan entry */ }
            )
        }

        // Level 3: Quick Stats Grid
        item {
            QuickStatsGrid(
                todaySpending = uiState.todaySpending,
                todayTransactionCount = uiState.todayTransactionCount,
                loanSummary = uiState.loanSummary
            )
        }

        // Level 4: Category Spending Summary
        item {
            CategorySpendingSummary(
                categories = uiState.spendingCategories
            )
        }

        // Level 5: Recent Activity
        item {
            RecentActivitySection(
                transactions = uiState.recentTransactions,
                onSeeAllClick = { /* TODO: Navigate to full transaction list */ }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    MonekoTheme {
        HomeScreen()
    }
}

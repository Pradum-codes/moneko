package com.pradumcodes.moneko.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.pradumcodes.moneko.navigation.Home
import com.pradumcodes.moneko.navigation.Loan
import com.pradumcodes.moneko.navigation.Report
import com.pradumcodes.moneko.navigation.Transaction
import com.pradumcodes.moneko.screens.home.HomeScreen
import com.pradumcodes.moneko.screens.loan.LoanScreen
import com.pradumcodes.moneko.screens.report.ReportsScreen
import com.pradumcodes.moneko.screens.transaction.TransactionScreen

fun NavGraphBuilder.rootGraph(
    navController: NavHostController
) {
    composable<Home> {
        HomeScreen()
    }

    composable<Transaction> {
        TransactionScreen()
    }

    composable<Report> {
        ReportsScreen()
    }

    composable<Loan> {
        LoanScreen()
    }
}
package com.pradumcodes.moneko.ui.app

import com.pradumcodes.moneko.R
import com.pradumcodes.moneko.navigation.Destination
import com.pradumcodes.moneko.navigation.Home
import com.pradumcodes.moneko.navigation.Loan
import com.pradumcodes.moneko.navigation.Report
import com.pradumcodes.moneko.navigation.Transaction

data class BottomBarItem(
    val destination: Destination,
    val label: String,
    val icon: Int
)

val BottomBarItems = listOf(
    BottomBarItem(Home, "Home", R.drawable.home_24px),
    BottomBarItem(Transaction, "Transactions", R.drawable.receipt_long_24px),
    BottomBarItem(Loan, "Loans", R.drawable.handshake_24px),
    BottomBarItem(Report, "Reports", R.drawable.bar_chart_24px)
)
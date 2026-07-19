package com.pradumcodes.moneko.ui.app

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.pradumcodes.moneko.navigation.Destination
import com.pradumcodes.moneko.navigation.Home
import com.pradumcodes.moneko.navigation.Loan
import com.pradumcodes.moneko.navigation.Report
import com.pradumcodes.moneko.navigation.Transaction

@Composable
fun BottomBar(
    currentDestination: NavDestination?,
    onDestinationSelected: (Destination) -> Unit
) {
    NavigationBar {
        BottomBarItems.forEach { item ->

            val selected = when(item.destination) {
                Home -> currentDestination?.hasRoute<Home>() == true
                Transaction -> currentDestination?.hasRoute<Transaction>() == true
                Loan -> currentDestination?.hasRoute<Loan>() == true
                Report -> currentDestination?.hasRoute<Report>() == true
            }


            NavigationBarItem(
                selected = selected,
                onClick = {
                    onDestinationSelected(item.destination)
                },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(item.label)
                }
            )
        }
    }
}
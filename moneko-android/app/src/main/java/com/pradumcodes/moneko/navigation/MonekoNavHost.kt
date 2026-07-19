package com.pradumcodes.moneko.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pradumcodes.moneko.navigation.graph.rootGraph

@Composable
fun MonekoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        rootGraph(navController)
    }
}
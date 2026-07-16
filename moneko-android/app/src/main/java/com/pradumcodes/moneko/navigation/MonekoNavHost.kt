package com.pradumcodes.moneko.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pradumcodes.moneko.navigation.graph.rootGraph

@Composable
fun MonekoNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        rootGraph(navController)
    }
}
package com.pradumcodes.moneko.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pradumcodes.moneko.navigation.graph.rootGraph

@RequiresApi(Build.VERSION_CODES.O)
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
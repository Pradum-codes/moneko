package com.pradumcodes.moneko.screens.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment

@Composable
fun TransactionScreen() {
    var count by rememberSaveable { mutableIntStateOf(0) }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Transactions")
        Text(text = "Test Object for navigation")
        Button(
            onClick = { count++ }
        ) {
            Text("$count")
        }
    }
}
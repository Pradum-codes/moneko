package com.pradumcodes.moneko.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Quick Actions Grid - shows buttons for adding expense, income, and loan
 */
@Composable
fun QuickActionsGrid(
    modifier: Modifier = Modifier,
    onAddExpenseClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {},
    onAddLoanClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionButton(modifier.weight(1f),"Add Expense") { onAddExpenseClick() }
        QuickActionButton(modifier.weight(1f),"Add Income") { onAddIncomeClick() }
        QuickActionButton(modifier.weight(1f),"Record Loan") { onAddLoanClick() }
    }
}

@Composable
fun QuickActionButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuickActionsGridPreview() {
    QuickActionsGrid()
}




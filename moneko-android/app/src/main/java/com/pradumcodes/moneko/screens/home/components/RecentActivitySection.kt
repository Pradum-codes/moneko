package com.pradumcodes.moneko.screens.home.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pradumcodes.moneko.screens.home.RecentTransactionUi
import com.pradumcodes.moneko.screens.home.TransactionType
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Individual transaction row item for recent activity
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentTransactionItem(
    modifier: Modifier = Modifier,
    transaction: RecentTransactionUi
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM, hh:mm a")
        .withZone(ZoneId.systemDefault())
    val formattedTime = formatter.format(transaction.timestamp)

    val (amountColor, amountPrefix) = when (transaction.type) {
        TransactionType.EXPENSE -> MaterialTheme.colorScheme.error to "-"
        TransactionType.INCOME -> MaterialTheme.colorScheme.primary to "+"
        TransactionType.LOAN -> MaterialTheme.colorScheme.tertiary to "±"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Leading Icon (Category icon placeholder)
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = amountColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transaction.title.take(1).uppercase(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = amountColor
            )
        }

        // Title and Timestamp
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        // Amount
        Text(
            text = "$amountPrefix₹${String.format(Locale.getDefault(), "%.2f", transaction.amount)}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = amountColor
        )
    }
}

/**
 * Recent Activity Section - displays transactions from the past 7 days
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentActivitySection(
    modifier: Modifier = Modifier,
    transactions: List<RecentTransactionUi> = emptyList(),
    onSeeAllClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        // Header with See All button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            TextButton(onClick = onSeeAllClick) {
                Text("See All")
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Transaction List
        if (transactions.isNotEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                transactions.take(5).forEach { transaction ->
                    RecentTransactionItem(transaction = transaction)
                }
            }
        } else {
            Text(
                text = "No transactions yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun RecentActivitySectionPreview() {
    val sampleTransactions = listOf(
        RecentTransactionUi(
            id = "1",
            title = "Starbucks",
            timestamp = Instant.now(),
            amount = 240.0,
            type = TransactionType.EXPENSE
        ),
        RecentTransactionUi(
            id = "2",
            title = "Salary",
            timestamp = Instant.now(),
            amount = 1000.0,
            type = TransactionType.INCOME
        )
    )
    RecentActivitySection(transactions = sampleTransactions)
}



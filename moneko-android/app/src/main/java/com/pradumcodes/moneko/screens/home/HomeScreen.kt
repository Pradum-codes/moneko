package com.pradumcodes.moneko.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pradumcodes.moneko.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val categories = uiState.categories
    val categoryMap = categories.associateBy { it.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = if (uiState.amount == 0L) "" else uiState.amount.toString(),
                        onValueChange = viewModel::updateAmount,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    OutlinedTextField(
                        value = uiState.note,
                        onValueChange = viewModel::updateNote,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Note") }
                    )

                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Category rows as their own items rather than one big
            // Column so they participate in the same scroll/measure
            // pass as everything else.
            items(
                items = categories,
                key = { it.id }
            ) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = uiState.category == category.id,
                        onClick = {
                            viewModel.updateCategory(category.id)
                        }
                    )

                    Text(
                        text = category.name,
                        modifier = Modifier.clickable {
                            viewModel.updateCategory(category.id)
                        }
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.submitExpense()
                        }
                    ) {
                        Text("Add Expense")
                    }

                    Text(
                        text = "Recent Transactions (${expenses.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            items(
                items = expenses,
                key = { it.id }   // assumes Expense has an `id`
            ) { expense ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        Text(
                            text = "₹${expense.amount}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = categoryMap[expense.categoryId]?.name
                                ?: "Unknown Category"
                        )

                        if (!expense.note.isNullOrBlank()) {
                            Text(
                                text = expense.note
                            )
                        }
                    }
                }
            }
        }
    }
}
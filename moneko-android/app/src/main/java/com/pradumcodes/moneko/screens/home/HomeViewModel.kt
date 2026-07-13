package com.pradumcodes.moneko.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pradumcodes.moneko.data.local.entity.CategoryEntity
import com.pradumcodes.moneko.data.local.entity.ExpenseEntity
import com.pradumcodes.moneko.data.repository.category.CategoryRepository
import com.pradumcodes.moneko.data.repository.expense.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

data class HomeUIState(
    var amount: Long = 0,
    var category: String = "",
    var note: String = "",
    var expenses: List<ExpenseEntity> = emptyList(),
    var categories: List<CategoryEntity> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState: StateFlow<HomeUIState> = _uiState.asStateFlow()

    val expenses: StateFlow<List<ExpenseEntity>> =
        expenseRepository.observeExpenses().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        // Load categories on initialization
        viewModelScope.launch {
            categoryRepository.observeCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    fun createExpense(
        amount: Long,
        category: String,
        note: String
    ) {
        viewModelScope.launch {
            expenseRepository.createExpense(
                amount = amount,
                categoryId = category,
                note = note
            )
        }
    }

    // Update helpers for UI bindings
    fun updateAmount(input: String) {
        val parsed = input.toLongOrNull() ?: 0L
        _uiState.update { it.copy(amount = parsed) }
    }

    fun updateCategory(input: String) {
        _uiState.update { it.copy(category = input) }
    }

    fun updateNote(input: String) {
        _uiState.update { it.copy(note = input) }
    }

    // Submit using the values currently in UI state
    fun submitExpense() {
        val current = _uiState.value
        viewModelScope.launch {
            expenseRepository.createExpense(
                amount = current.amount,
                categoryId = current.category,
                note = current.note
            )

            // Clear inputs after creating expense
            _uiState.update { it.copy(amount = 0L, category = "", note = "") }
        }
    }

    fun getCategories(): Flow<List<CategoryEntity>> {
        return categoryRepository.observeCategories()
    }
}
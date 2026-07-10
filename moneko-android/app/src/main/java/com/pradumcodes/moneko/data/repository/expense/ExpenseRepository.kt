package com.pradumcodes.moneko.data.repository.expense

import com.pradumcodes.moneko.data.local.dao.ExpenseDao
import com.pradumcodes.moneko.data.local.entity.ExpenseEntity
import com.pradumcodes.moneko.util.SyncState
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class ExpenseRepository(
    private val expenseDao: ExpenseDao
) {
    suspend fun createExpense(amount: Long, categoryId: String, note: String?) {
        require(amount > 0) {
            "Amount should be greater than zero."
        }
        require(categoryId.isNotEmpty()) {
            "Category ID cannot be blank."
        }

        val expense = ExpenseEntity(
            id = newId(),
            amount = amount,
            categoryId = categoryId,
            note = note,
            createdAt = currentTime(),
            updatedAt = currentTime(),
            syncState = SyncState.LOCAL_ONLY,
            localVersion = 1,
            isDeleted = false
        )

        expenseDao.insert(expense)
    }

    suspend fun updateExpense(
        expenseId: String,
        amount: Long,
        categoryId: String,
        note: String?
    ) {
        val existingExpense = expenseDao.getExpenseById(expenseId)
            ?: throw IllegalArgumentException("Expense not found.")
        require(amount > 0)

        val cleanedCategoryId = categoryId.trim()

        require(cleanedCategoryId.isNotBlank()) {
            "Category ID cannot be blank."
        }

        val cleanedNote = note?.trim()

        require((cleanedNote == null) || cleanedNote.isNotBlank()) {
            "Note cannot be blank."
        }

        val updatedExpense = existingExpense.copy(
            amount = amount,
            categoryId = categoryId.trim(),
            note = note?.trim(),
            updatedAt = currentTime(),
            localVersion = existingExpense.localVersion + 1,
            syncState = SyncState.LOCAL_ONLY
        )

        expenseDao.update(updatedExpense)
    }

    suspend fun deleteExpense(expenseId: String) {
        expenseDao.softDeleteExpense(
            expenseId = expenseId,
            updatedAt = System.currentTimeMillis()
        )
    }

    suspend fun restoreExpense(expenseId: String) {
        expenseDao.restoreExpense(
            expenseId = expenseId,
            updatedAt = System.currentTimeMillis()
        )
    }

    fun observeExpenses(): Flow<List<ExpenseEntity>> =
        expenseDao.getAllExpensesFlow()

    suspend fun getExpense(expenseId: String): ExpenseEntity? =
        expenseDao.getExpenseById(expenseId)

    suspend fun getExpensesBetween(from: Long, to: Long): List<ExpenseEntity> =
        expenseDao.getExpensesBetween(from, to)

    suspend fun getTotalExpenseBetween(from: Long, to: Long): Long =
        expenseDao.getTotalExpenseBetween(from, to)

    fun observeExpensesByCategory(categoryId: String) =
        expenseDao.getExpensesByCategoryFlow(categoryId)

    fun searchExpenses(query: String) =
        expenseDao.searchExpensesFlow(query)

    private fun currentTime() =
        System.currentTimeMillis()

    private fun newId() =
        UUID.randomUUID().toString()

}
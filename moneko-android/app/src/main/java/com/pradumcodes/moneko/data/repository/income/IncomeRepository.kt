package com.pradumcodes.moneko.data.repository.income

import com.pradumcodes.moneko.data.local.dao.IncomeDao
import com.pradumcodes.moneko.data.local.entity.IncomeEntity
import com.pradumcodes.moneko.util.SyncState
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class IncomeRepository @Inject constructor (
    private val incomeDao: IncomeDao
) {

    suspend fun createIncome(
        amount: Long,
        categoryId: String,
        note: String? = null
    ) {

        require(amount > 0) {
            "Amount should be greater than zero."
        }

        val cleanedCategoryId = categoryId.trim()

        require(cleanedCategoryId.isNotEmpty()) {
            "Category ID cannot be blank."
        }

        val cleanedNote = note?.trim()

        require(cleanedNote == null || cleanedNote.isNotBlank()) {
            "Note cannot be blank."
        }

        val now = currentTime()

        val income = IncomeEntity(
            id = newId(),
            amount = amount,
            categoryId = cleanedCategoryId,
            note = cleanedNote,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.LOCAL_ONLY,
            localVersion = 1,
            isDeleted = false
        )

        incomeDao.insert(income)
    }

    suspend fun updateIncome(
        incomeId: String,
        amount: Long,
        categoryId: String,
        note: String?
    ) {

        require(amount > 0) {
            "Amount should be greater than zero."
        }

        val cleanedCategoryId = categoryId.trim()

        require(cleanedCategoryId.isNotBlank()) {
            "Category ID cannot be blank."
        }

        val cleanedNote = note?.trim()

        require(cleanedNote == null || cleanedNote.isNotBlank()) {
            "Note cannot be blank."
        }

        val existingIncome = incomeDao.getIncomeById(incomeId)
            ?: throw IllegalArgumentException("Income not found.")

        val updatedIncome = existingIncome.copy(
            amount = amount,
            categoryId = cleanedCategoryId,
            note = cleanedNote,
            updatedAt = currentTime(),
            localVersion = existingIncome.localVersion + 1,
            syncState = SyncState.LOCAL_ONLY
        )

        incomeDao.update(updatedIncome)
    }

    suspend fun deleteIncome(incomeId: String) {
        incomeDao.softDeleteIncome(
            incomeId = incomeId,
            updatedAt = currentTime()
        )
    }

    suspend fun restoreIncome(incomeId: String) {
        incomeDao.restoreIncome(
            incomeId = incomeId,
            updatedAt = currentTime()
        )
    }

    suspend fun getIncome(incomeId: String): IncomeEntity? =
        incomeDao.getIncomeById(incomeId)

    fun observeIncomes(): Flow<List<IncomeEntity>> =
        incomeDao.getAllIncomes()

    fun observeIncomesByCategory(categoryId: String): Flow<List<IncomeEntity>> =
        incomeDao.getIncomesByCategory(categoryId)

    suspend fun getIncomesBetween(
        from: Long,
        to: Long
    ): List<IncomeEntity> =
        incomeDao.getIncomesBetween(from, to)

    suspend fun getTotalIncome(): Long =
        incomeDao.getTotalIncome()

    suspend fun getTotalIncomeBetween(
        from: Long,
        to: Long
    ): Long =
        incomeDao.getTotalIncomeBetween(from, to)

    fun searchIncomes(query: String): Flow<List<IncomeEntity>> =
        incomeDao.searchIncomesFlow(query)

    private fun currentTime(): Long =
        System.currentTimeMillis()

    private fun newId(): String =
        UUID.randomUUID().toString()
}
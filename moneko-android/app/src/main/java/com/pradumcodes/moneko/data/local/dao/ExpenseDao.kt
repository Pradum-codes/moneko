package com.pradumcodes.moneko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pradumcodes.moneko.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExpense(expense: ExpenseEntity)

    @Query("""
        SELECT *
        FROM expense
        WHERE isDeleted = 0
        ORDER BY createdAt DESC
    """)
    fun getAllExpensesFlow(): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT *
        FROM expense
        WHERE isDeleted = 0
        ORDER BY createdAt DESC
    """)
    suspend fun getAllExpenses(): List<ExpenseEntity>

    @Query("SELECT * FROM expense WHERE id = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: String): ExpenseEntity?

    @Query("""
        UPDATE expense
        SET isDeleted = 1,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :expenseId
    """)
    suspend fun softDeleteExpense(expenseId: String, updatedAt: Long)

    @Query("""
        UPDATE expense
        SET isDeleted = 0,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :expenseId
    """)
    suspend fun restoreExpense(expenseId: String, updatedAt: Long)

    @Query("""
        SELECT *
        FROM expense
        WHERE isDeleted = 0
          AND createdAt BETWEEN :from AND :to
        ORDER BY createdAt DESC
    """)
    suspend fun getExpensesBetween(from: Long, to: Long): List<ExpenseEntity>

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM expense
        WHERE isDeleted = 0
          AND createdAt BETWEEN :from AND :to
    """)
    suspend fun getTotalExpenseBetween(from: Long, to: Long): Long

    @Query("""
        SELECT *
        FROM expense
        WHERE isDeleted = 0
          AND categoryId = :categoryId
        ORDER BY createdAt DESC
    """)
    fun getExpensesByCategoryFlow(categoryId: String): Flow<List<ExpenseEntity>>

    @Query("""
        SELECT *
        FROM expense
        WHERE isDeleted = 0
          AND LOWER(note) LIKE LOWER('%' || :query || '%')
        ORDER BY createdAt DESC
    """)
    fun searchExpensesFlow(query: String): Flow<List<ExpenseEntity>>
}

package com.pradumcodes.moneko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pradumcodes.moneko.data.local.entity.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertIncome(income: IncomeEntity)

    @Query("""
        SELECT *
        FROM income
        WHERE isDeleted = 0
        ORDER BY createdAt DESC
    """)
    fun getAllIncomesFlow(): Flow<List<IncomeEntity>>

    @Query("""
        SELECT *
        FROM income
        WHERE isDeleted = 0
        ORDER BY createdAt DESC
    """)
    suspend fun getAllIncomes(): List<IncomeEntity>

    @Query("SELECT * FROM income WHERE id = :incomeId LIMIT 1")
    suspend fun getIncomeById(incomeId: String): IncomeEntity?

    @Query("""
        UPDATE income
        SET isDeleted = 1,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :incomeId
    """)
    suspend fun softDeleteIncome(incomeId: String, updatedAt: Long)

    @Query("""
        UPDATE income
        SET isDeleted = 0,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :incomeId
    """)
    suspend fun restoreIncome(incomeId: String, updatedAt: Long)

    @Query("""
        SELECT *
        FROM income
        WHERE isDeleted = 0
          AND createdAt BETWEEN :from AND :to
        ORDER BY createdAt DESC
    """)
    suspend fun getIncomesBetween(from: Long, to: Long): List<IncomeEntity>

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM income
        WHERE isDeleted = 0
          AND createdAt BETWEEN :from AND :to
    """)
    suspend fun getTotalIncomeBetween(from: Long, to: Long): Long

    @Query("""
        SELECT *
        FROM income
        WHERE isDeleted = 0
          AND LOWER(note) LIKE LOWER('%' || :query || '%')
        ORDER BY createdAt DESC
    """)
    fun searchIncomesFlow(query: String): Flow<List<IncomeEntity>>
}

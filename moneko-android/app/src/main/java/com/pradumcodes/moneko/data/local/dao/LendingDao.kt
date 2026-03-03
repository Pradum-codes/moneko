package com.pradumcodes.moneko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pradumcodes.moneko.data.local.entity.LendingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LendingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLending(lending: LendingEntity)

    // Counts
    @Query("SELECT COUNT(*) FROM lending WHERE isDeleted = 0")
    suspend fun getLendingCount(): Int

    @Query("SELECT COUNT(*) FROM lending WHERE isDeleted = 0")
    fun getLendingCountFlow(): Flow<Int>

    // Totals (unsettled only)
    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM lending
        WHERE type = 'LENT'
          AND isSettled = 0
          AND isDeleted = 0
    """)
    suspend fun getTotalAmountLent(): Long

    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM lending
        WHERE type = 'BORROWED'
          AND isSettled = 0
          AND isDeleted = 0
    """)
    suspend fun getTotalAmountBorrowed(): Long

    // Lists
    @Query("""
        SELECT *
        FROM lending
        WHERE isDeleted = 0
        ORDER BY createdAt DESC
    """)
    fun getAllLendingsFlow(): Flow<List<LendingEntity>>

    @Query("""
        SELECT *
        FROM lending
        WHERE isDeleted = 0
        ORDER BY createdAt DESC
    """)
    suspend fun getAllLendings(): List<LendingEntity>

    @Query("""
        SELECT *
        FROM lending
        WHERE id = :lendingId
        LIMIT 1
    """)
    suspend fun getLendingById(lendingId: String): LendingEntity?

    // Unsettled only
    @Query("""
        SELECT *
        FROM lending
        WHERE isDeleted = 0
          AND isSettled = 0
        ORDER BY createdAt DESC
    """)
    fun getUnsettledLendingsFlow(): Flow<List<LendingEntity>>

    // Settle / unsettle (sync-safe)
    @Query("""
        UPDATE lending
        SET isSettled = :isSettled,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :lendingId
    """)
    suspend fun setSettledState(
        lendingId: String,
        isSettled: Boolean,
        updatedAt: Long
    )

    // Range
    @Query("""
        SELECT *
        FROM lending
        WHERE isDeleted = 0
          AND createdAt BETWEEN :from AND :to
        ORDER BY createdAt DESC
    """)
    suspend fun getLendingsBetween(from: Long, to: Long): List<LendingEntity>

    // Search
    @Query("""
        SELECT *
        FROM lending
        WHERE isDeleted = 0
          AND LOWER(note) LIKE LOWER('%' || :query || '%')
        ORDER BY createdAt DESC
    """)
    fun searchLendingsFlow(query: String): Flow<List<LendingEntity>>
}

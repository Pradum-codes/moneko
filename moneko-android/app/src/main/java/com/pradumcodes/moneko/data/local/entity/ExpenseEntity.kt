package com.pradumcodes.moneko.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pradumcodes.moneko.util.SyncState

@Entity(
    tableName = "expense",
    indices = [
        Index("createdAt"),
        Index("categoryId"),
        Index("syncState")
    ]
)
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val amount: Long,              // Smallest currency unit (e.g., paise)
    val categoryId: String,        // FK to CategoryEntity.id
    val note: String?,
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)

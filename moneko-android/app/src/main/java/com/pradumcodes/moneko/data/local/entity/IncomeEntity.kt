package com.pradumcodes.moneko.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pradumcodes.moneko.util.SyncState

@Entity(
    tableName = "income",
    indices = [
        Index("createdAt"),
        Index("source"),
        Index("syncState")
    ]
)
data class IncomeEntity(
    @PrimaryKey val id: String,
    val amount: Long,           // Smallest currency unit (e.g., paise, cents)
    val source: String,         // Income source (salary, freelance, etc.)
    val note: String?,          // Optional description
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)
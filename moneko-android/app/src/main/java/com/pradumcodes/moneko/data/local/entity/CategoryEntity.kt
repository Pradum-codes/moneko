package com.pradumcodes.moneko.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pradumcodes.moneko.util.SyncState

enum class CategoryType {
    EXPENSE,
    INCOME
}

@Entity(
    tableName = "category",
    indices = [
        Index(value = ["name", "type"], unique = true)
    ]
)
data class CategoryEntity(
    @PrimaryKey val id: String,        // UUID
    val name: String,                  // "Groceries", "Salary"
    val type: CategoryType,            // EXPENSE / INCOME
    val isSystemDefined: Boolean,      // true = predefined
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)

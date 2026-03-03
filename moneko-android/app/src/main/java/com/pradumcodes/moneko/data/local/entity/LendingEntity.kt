package com.pradumcodes.moneko.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pradumcodes.moneko.util.SyncState

enum class LendingType {
    LENT,      // Money given to someone else
    BORROWED   // Money received from someone else
}

@Entity(
    tableName = "lending",
    indices = [
        Index("createdAt"),
        Index("counterparty"),
        Index("type"),
        Index("syncState")
    ]
)
data class LendingEntity(
    @PrimaryKey val id: String,
    val type: LendingType,      // LENT or BORROWED
    val amount: Long,           // Smallest currency unit
    val counterparty: String,   // Person/entity involved
    val note: String?,          // Optional description
    val isSettled: Boolean = false,  // Settlement status
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)
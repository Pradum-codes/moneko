package com.pradumcodes.moneko.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.pradumcodes.moneko.util.SyncState

@Entity(
    tableName = "income",
    indices = [
        Index("createdAt"),
        Index("categoryId"),
        Index("syncState")
    ],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class IncomeEntity(
    @PrimaryKey val id: String,
    var amount: Long,           // Smallest currency unit (e.g., paise, cents)
    val categoryId: String,     // FK to CategoryEntity.id
    val note: String?,          // Optional description
    val createdAt: Long,
    val updatedAt: Long,
    val syncState: SyncState,
    val localVersion: Int,
    val isDeleted: Boolean
)
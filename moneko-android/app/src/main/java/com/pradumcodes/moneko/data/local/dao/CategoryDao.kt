package com.pradumcodes.moneko.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pradumcodes.moneko.data.local.entity.CategoryEntity
import com.pradumcodes.moneko.data.local.entity.CategoryType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Query("""
        SELECT *
        FROM category
        WHERE isDeleted = 0
        ORDER BY name ASC
    """)
    fun getAllCategoriesFlow(): Flow<List<CategoryEntity>>

    @Query("""
        SELECT *
        FROM category
        WHERE isDeleted = 0
        ORDER BY name ASC
    """)
    suspend fun getAllCategories(): List<CategoryEntity>

    @Query("""
        SELECT *
        FROM category
        WHERE isDeleted = 0
          AND type = :type
        ORDER BY name ASC
    """)
    fun getCategoriesByTypeFlow(type: CategoryType): Flow<List<CategoryEntity>>

    @Query("""
        SELECT *
        FROM category
        WHERE id = :categoryId
        LIMIT 1
    """)
    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    @Query("""
        UPDATE category
        SET isDeleted = 1,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :categoryId
          AND isSystemDefined = 0
    """)
    suspend fun softDeleteCategory(categoryId: String, updatedAt: Long)

    @Query("""
        UPDATE category
        SET isDeleted = 0,
            updatedAt = :updatedAt,
            localVersion = localVersion + 1
        WHERE id = :categoryId
    """)
    suspend fun restoreCategory(categoryId: String, updatedAt: Long)

    @Query("""
        SELECT COUNT(*)
        FROM category
        WHERE isDeleted = 0
    """)
    suspend fun getActiveCategoryCount(): Int
}

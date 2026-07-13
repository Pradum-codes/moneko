package com.pradumcodes.moneko.data.repository.category

import com.pradumcodes.moneko.data.local.dao.CategoryDao
import com.pradumcodes.moneko.data.local.entity.CategoryEntity
import com.pradumcodes.moneko.data.local.entity.CategoryType
import com.pradumcodes.moneko.util.SyncState
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

private data class DefaultCategory(
    val name: String,
    val icon: String
)

private val DEFAULT_EXPENSE_CATEGORIES = listOf(
    DefaultCategory("Groceries", "cat_groceries"),
    DefaultCategory("Food", "cat_food"),
    DefaultCategory("Transport", "cat_transport"),
    DefaultCategory("Rent", "cat_rent"),
    DefaultCategory("Utilities", "cat_utilities"),
    DefaultCategory("Entertainment", "cat_entertainment"),
    DefaultCategory("Health", "cat_health")
)

private val DEFAULT_INCOME_CATEGORIES = listOf(
    DefaultCategory("Salary", "cat_salary"),
    DefaultCategory("Pocket Money", "cat_pocket_money"),
    DefaultCategory("Business", "cat_business"),
    DefaultCategory("Interest", "cat_interest")
)

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {

    suspend fun seedDefaultCategories() {

        if (categoryDao.getActiveCategoryCount() > 0) {
            return
        }

        val now = currentTime()

        val categories = buildList {

            DEFAULT_EXPENSE_CATEGORIES.forEach { category ->
                add(
                    createDefaultCategory(
                        name = category.name,
                        icon = category.icon,
                        type = CategoryType.EXPENSE,
                        now = now
                    )
                )
            }

            DEFAULT_INCOME_CATEGORIES.forEach { category ->
                add(
                    createDefaultCategory(
                        name = category.name,
                        icon = category.icon,
                        type = CategoryType.INCOME,
                        now = now
                    )
                )
            }
        }

        categoryDao.insertAll(categories)
    }

    suspend fun createCategory(
        name: String,
        type: CategoryType,
        icon: String? = null,
        color: Long? = null
    ) {

        val trimmedName = name.trim()

        require(trimmedName.isNotEmpty()) {
            "Category name cannot be blank."
        }

        val now = currentTime()

        val category = CategoryEntity(
            id = UUID.randomUUID().toString(),
            name = trimmedName,
            type = type,
            isSystemDefined = false,
            createdAt = now,
            updatedAt = now,
            syncState = SyncState.LOCAL_ONLY,
            localVersion = 1,
            isDeleted = false,
            icon = icon,
            color = color
        )

        categoryDao.insert(category)
    }

    suspend fun deleteCategory(categoryId: String) {
        categoryDao.softDeleteCategory(
            categoryId = categoryId,
            updatedAt = currentTime()
        )
    }

    suspend fun restoreCategory(categoryId: String) {
        categoryDao.restoreCategory(
            categoryId = categoryId,
            updatedAt = currentTime()
        )
    }

    fun observeCategories(): Flow<List<CategoryEntity>> =
        categoryDao.getAllCategories()

    fun observeExpenseCategories(): Flow<List<CategoryEntity>> =
        categoryDao.getCategoriesByType(CategoryType.EXPENSE)

    fun observeIncomeCategories(): Flow<List<CategoryEntity>> =
        categoryDao.getCategoriesByType(CategoryType.INCOME)

    suspend fun getCategory(categoryId: String): CategoryEntity? =
        categoryDao.getCategoryById(categoryId)

    private fun createDefaultCategory(
        name: String,
        icon: String,
        type: CategoryType,
        now: Long
    ) = CategoryEntity(
        id = UUID.randomUUID().toString(),
        name = name,
        type = type,
        isSystemDefined = true,
        createdAt = now,
        updatedAt = now,
        syncState = SyncState.SYNCED,
        localVersion = 1,
        isDeleted = false,
        icon = icon
    )

    private fun currentTime(): Long = System.currentTimeMillis()
}
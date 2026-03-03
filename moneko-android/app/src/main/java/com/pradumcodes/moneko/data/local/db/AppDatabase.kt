package com.pradumcodes.moneko.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pradumcodes.moneko.data.local.dao.CategoryDao
import com.pradumcodes.moneko.data.local.dao.ExpenseDao
import com.pradumcodes.moneko.data.local.dao.IncomeDao
import com.pradumcodes.moneko.data.local.dao.LendingDao
import com.pradumcodes.moneko.data.local.entity.CategoryEntity
import com.pradumcodes.moneko.data.local.entity.ExpenseEntity
import com.pradumcodes.moneko.data.local.entity.IncomeEntity
import com.pradumcodes.moneko.data.local.entity.LendingEntity

@Database(
    entities = [
        CategoryEntity::class,
        ExpenseEntity::class,
        IncomeEntity::class,
        LendingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun incomeDao(): IncomeDao
    abstract fun lendingDao(): LendingDao
}

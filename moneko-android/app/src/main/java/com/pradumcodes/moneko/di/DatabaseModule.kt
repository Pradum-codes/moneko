package com.pradumcodes.moneko.di

import android.content.Context
import androidx.room.Room
import com.pradumcodes.moneko.data.local.dao.CategoryDao
import com.pradumcodes.moneko.data.local.dao.ExpenseDao
import com.pradumcodes.moneko.data.local.dao.IncomeDao
import com.pradumcodes.moneko.data.local.dao.LendingDao
import com.pradumcodes.moneko.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "moneko.db"
        )
            .fallbackToDestructiveMigrationOnDowngrade(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseDao(
        database: AppDatabase
    ): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideIncomeDao(
        database: AppDatabase
    ): IncomeDao {
        return database.incomeDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(
        database: AppDatabase
    ): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideLendingDao(
        database: AppDatabase
    ): LendingDao {
        return database.lendingDao()
    }
}

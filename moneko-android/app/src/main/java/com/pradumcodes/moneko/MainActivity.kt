package com.pradumcodes.moneko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.pradumcodes.moneko.data.local.dao.CategoryDao
import com.pradumcodes.moneko.data.local.db.DatabaseProvider
import com.pradumcodes.moneko.data.local.entity.CategoryEntity
import com.pradumcodes.moneko.data.local.entity.CategoryType
import com.pradumcodes.moneko.ui.theme.MonekoTheme
import com.pradumcodes.moneko.util.SyncState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val context = applicationContext
    val db = DatabaseProvider.getDatabase(context)

    // Launch a coroutine tied to the Activity lifecycle and run DB work on IO dispatcher
    lifecycleScope.launch {
        withContext(Dispatchers.IO) {
            seedDefaultCategories(db.categoryDao())
        }
    }

    enableEdgeToEdge()
    setContent {
        MonekoTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                App(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}
}

@Composable
fun App(modifier: Modifier) {
    Text(text = "Hello, Moneko!")
}

private val DEFAULT_EXPENSE_CATEGORIES = listOf(
    "Groceries",
    "Food",
    "Transport",
    "Rent",
    "Utilities",
    "Entertainment",
    "Health"
)

private val DEFAULT_INCOME_CATEGORIES = listOf(
    "Salary",
    "Pocket Money",
    "Business",
    "Interest"
)

suspend fun seedDefaultCategories(categoryDao: CategoryDao) {
    val now = System.currentTimeMillis()

    val categories = buildList {

        DEFAULT_EXPENSE_CATEGORIES.forEach { name ->
            add(
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    type = CategoryType.EXPENSE,
                    isSystemDefined = true,
                    createdAt = now,
                    updatedAt = now,
                    syncState = SyncState.SYNCED,
                    localVersion = 1,
                    isDeleted = false
                )
            )
        }

        DEFAULT_INCOME_CATEGORIES.forEach { name ->
            add(
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    type = CategoryType.INCOME,
                    isSystemDefined = true,
                    createdAt = now,
                    updatedAt = now,
                    syncState = SyncState.SYNCED,
                    localVersion = 1,
                    isDeleted = false
                )
            )
        }
    }

    categoryDao.insertAll(categories)
}
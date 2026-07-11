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
import com.pradumcodes.moneko.data.local.db.DatabaseProvider
import com.pradumcodes.moneko.data.repository.category.CategoryRepository
import com.pradumcodes.moneko.ui.theme.MonekoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        val db = DatabaseProvider.getDatabase(context)
        val categoryRepository = CategoryRepository(db.categoryDao())

        // Launch a coroutine tied to the Activity lifecycle and run DB work on IO dispatcher
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                categoryRepository.seedDefaultCategories()
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

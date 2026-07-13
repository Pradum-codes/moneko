package com.pradumcodes.moneko

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.lifecycleScope
import com.pradumcodes.moneko.data.repository.category.CategoryRepository
import com.pradumcodes.moneko.screens.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var categoryRepository: CategoryRepository

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Launch a coroutine tied to the Activity lifecycle and run DB work on IO dispatcher
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                categoryRepository.seedDefaultCategories()
            }
        }

        enableEdgeToEdge()
        setContent {
            HomeScreen()
        }
    }
}

package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.MainAppScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.AppViewModelFactory

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "attend_well_database"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    private val repository by lazy {
        AppRepository(
            studentDao = database.studentDao(),
            attendanceDao = database.attendanceDao(),
            leaveDao = database.leaveDao(),
            wellnessDao = database.wellnessDao(),
            studentFeeDao = database.studentFeeDao(),
            organizationDao = database.organizationDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Simple DI Factory definition inside Content context
            val viewModelFactory = AppViewModelFactory(repository)
            val viewModel = androidx.lifecycle.viewmodel.compose.viewModel<AppViewModel>(factory = viewModelFactory)
            val isDark by viewModel.isDarkMode.collectAsState()

            MyApplicationTheme(darkTheme = isDark) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MainAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}


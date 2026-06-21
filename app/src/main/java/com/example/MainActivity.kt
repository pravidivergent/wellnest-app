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
import androidx.lifecycle.lifecycleScope
import com.example.data.FirestoreSyncManager

class MainActivity : ComponentActivity() {
    
    private val database by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    private val repository by lazy {
        AppRepository(
            studentDao = database.studentDao(),
            attendanceDao = database.attendanceDao(),
            leaveDao = database.leaveDao(),
            wellnessDao = database.wellnessDao(),
            studentFeeDao = database.studentFeeDao(),
            organizationDao = database.organizationDao(),
            userAccountDao = database.userAccountDao(),
            coachDao = database.coachDao(),
            tournamentDao = database.tournamentDao(),
            studentDocumentDao = database.studentDocumentDao(),
            automatedEmailAlertDao = database.automatedEmailAlertDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ask for Notification Permissions on Android 13+ (API 33)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
            if (permissionCheck != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // Schedule Daily Recovery / Wellness Check-In reminders
        DailyWellnessReminderReceiver.scheduleDailyReminder(applicationContext)

        // Initialize Cloud Synchronization Layer
        val syncManager = FirestoreSyncManager(applicationContext, database)
        repository.setSyncManager(syncManager)
        
        // Trigger background real-time bidirectional synchronization
        if (syncManager.isCloudAvailable()) {
            syncManager.startRealtimeSync(lifecycleScope)
        }

        setContent {
            // Simple DI Factory definition inside Content context
            val viewModelFactory = AppViewModelFactory(repository, applicationContext)
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


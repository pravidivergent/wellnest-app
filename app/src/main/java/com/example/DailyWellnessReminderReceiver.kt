package com.example

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.data.AppDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DailyWellnessReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("WellnessReminder", "onReceive action = $action")

        if (action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule alarm on boot completed
            scheduleDailyReminder(context)
            return
        }

        // It is our reminder alarm
        val goAsync = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val prefs = context.getSharedPreferences("AthlePulsePrefs", Context.MODE_PRIVATE)
                val isEnabled = prefs.getBoolean("reminderEnabled", true)
                if (!isEnabled) {
                    Log.d("WellnessReminder", "Reminders are disabled by the user.")
                    return@launch
                }

                val regNum = prefs.getString("loggedInStudentReg", "") ?: ""
                val studentName = prefs.getString("loggedInStudentName", "Athlete") ?: "Athlete"

                if (regNum.isNotEmpty()) {
                    val db = AppDatabase.getInstance(context)
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val existingEntry = db.wellnessDao().getWellnessEntryForDateDirect(regNum, today)

                    if (existingEntry == null) {
                        Log.d("WellnessReminder", "No wellness entry registered for $regNum on $today. Post Notification!")
                        showReminderNotification(context, studentName)
                    } else {
                        Log.d("WellnessReminder", "Wellness entry already registered for $regNum today. Skip reminder.")
                    }
                } else {
                    Log.d("WellnessReminder", "No student currently logged in. Post general wellness reminder.")
                    showReminderNotification(context, "Athlete")
                }

                // Schedule next day's reminder alarm
                scheduleDailyReminder(context)
            } catch (e: Exception) {
                Log.e("WellnessReminder", "Error checking wellness check-in", e)
            } finally {
                goAsync.finish()
            }
        }
    }

    private fun showReminderNotification(context: Context, athleteName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "wellness_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Daily Wellness Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminds you to check-in your wellness scores daily to keep metrics updated."
                enableLights(true)
                lightColor = android.graphics.Color.GREEN
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Open app main activity on click
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(context, 1001, clickIntent, flags)

        val greeting = if (athleteName != "Athlete") "Hi $athleteName! 👋" else "Greetings Athlete! 👋"
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder) // Use default system icon safely
            .setContentTitle("AthlePulse Wellness Check-In 🧠")
            .setContentText("$greeting Take a moment to log your mood, sleep & energy levels today.")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "$greeting\n\nYour sports-health metrics are a vital part of your training and recovery! You have not logged your sleep, mood, or physical energy scores for today yet. Fill out your check-in card now to populate your live performance radar!"
            ))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)

        notificationManager.notify(4829, builder.build())
    }

    companion object {
        fun scheduleDailyReminder(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, DailyWellnessReminderReceiver::class.java)
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
            val pendingIntent = PendingIntent.getBroadcast(context, 1002, intent, flags)

            // Read hour and minutes from prefs
            val prefs = context.getSharedPreferences("AthlePulsePrefs", Context.MODE_PRIVATE)
            val isEnabled = prefs.getBoolean("reminderEnabled", true)

            if (!isEnabled) {
                alarmManager.cancel(pendingIntent)
                Log.d("WellnessReminder", "Cancelled scheduled daily alarm since reminders are disabled.")
                return
            }

            val hour = prefs.getInt("reminderHour", 19) // default 7 PM
            val minute = prefs.getInt("reminderMinute", 0)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // If scheduled time is in the past, schedule for tomorrow
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.time)
                Log.d("WellnessReminder", "Scheduled wellness notification alarm at $format")
            } catch (e: SecurityException) {
                Log.e("WellnessReminder", "Could not set alarm due to security restrictions", e)
            }
        }

        fun triggerNotificationInstantly(context: Context) {
            // Helper to immediately test trigger the notification for demonstration!
            val prefs = context.getSharedPreferences("AthlePulsePrefs", Context.MODE_PRIVATE)
            val studentName = prefs.getString("loggedInStudentName", "Athlete") ?: "Athlete"
            val receiver = DailyWellnessReminderReceiver()
            receiver.showReminderNotification(context, studentName)
        }
    }
}

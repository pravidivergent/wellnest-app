package com.example.data

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirestoreSyncManager(
    private val context: Context,
    private val database: AppDatabase
) {
    private var firestore: FirebaseFirestore? = null

    init {
        try {
            // Check if Firebase is already initialized, otherwise initialize it.
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            firestore = FirebaseFirestore.getInstance()
            Log.d("FirestoreSyncManager", "Firebase Firestore initialized!")
        } catch (e: Exception) {
            Log.e("FirestoreSyncManager", "Firebase initialization failed: ${e.message}. Falling back to local database.")
        }
    }

    fun isCloudAvailable(): Boolean {
        return firestore != null
    }

    // Real-Time Listeners to Sync Documents from Firestore Cloud -> Room Database
    fun startRealtimeSync(scope: CoroutineScope) {
        val fs = firestore ?: return
        Log.d("FirestoreSyncManager", "Configuring real-time Firestore listeners for active data sync...")

        // 1. User Accounts Sync
        safelyListen(fs, "user_accounts") { change ->
            val u = UserAccount(
                phoneNumber = change["phoneNumber"] as? String ?: "",
                password = change["password"] as? String ?: "",
                role = change["role"] as? String ?: "",
                registerNumber = change["registerNumber"] as? String ?: "",
                academyName = change["academyName"] as? String ?: "",
                hasAccess = change["hasAccess"] as? Boolean ?: true
            )
            if (u.phoneNumber.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.userAccountDao().insertAccount(u) }
            }
        }

        // 2. Student Profiles Sync
        safelyListen(fs, "student_profiles") { change ->
            val s = StudentProfile(
                registerNumber = change["registerNumber"] as? String ?: "",
                name = change["name"] as? String ?: "",
                address = change["address"] as? String ?: "",
                mobileNumber = change["mobileNumber"] as? String ?: "",
                parentMobile = change["parentMobile"] as? String ?: "",
                batch = change["batch"] as? String ?: "",
                course = change["course"] as? String ?: "",
                profilePhoto = change["profilePhoto"] as? String ?: "avatar_1",
                academyName = change["academyName"] as? String ?: ""
            )
            if (s.registerNumber.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.studentDao().insertStudentProfile(s) }
            }
        }

        // 3. Attendance Records Sync
        safelyListen(fs, "attendance_records") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val record = AttendanceRecord(
                id = id,
                registerNumber = change["registerNumber"] as? String ?: "",
                date = change["date"] as? String ?: "",
                shift = change["shift"] as? String ?: "",
                status = change["status"] as? String ?: "",
                timestamp = change["timestamp"] as? Long ?: System.currentTimeMillis()
            )
            if (record.registerNumber.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.attendanceDao().insertAttendance(record) }
            }
        }

        // 4. Leave Applications Sync
        safelyListen(fs, "leave_applications") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val leave = LeaveApplication(
                id = id,
                studentRegister = change["studentRegister"] as? String ?: "",
                studentName = change["studentName"] as? String ?: "",
                startDate = change["startDate"] as? String ?: "",
                endDate = change["endDate"] as? String ?: "",
                reason = change["reason"] as? String ?: "",
                proofName = change["proofName"] as? String ?: "",
                status = change["status"] as? String ?: "Pending",
                remarks = change["remarks"] as? String ?: ""
            )
            if (leave.studentRegister.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.leaveDao().insertLeave(leave) }
            }
        }

        // 5. Wellness Entries Sync
        safelyListen(fs, "wellness_entries") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val entry = WellnessEntry(
                id = id,
                registerNumber = change["registerNumber"] as? String ?: "",
                date = change["date"] as? String ?: "",
                sleepHours = (change["sleepHours"] as? Double)?.toFloat() ?: 0.0f,
                hadBreakfast = change["hadBreakfast"] as? Boolean ?: false,
                hadLunch = change["hadLunch"] as? Boolean ?: false,
                hadDinner = change["hadDinner"] as? Boolean ?: false,
                waterIntakeCups = (change["waterIntakeCups"] as? Long)?.toInt() ?: 0,
                energyLevel = (change["energyLevel"] as? Long)?.toInt() ?: 5,
                mood = change["mood"] as? String ?: "Calm",
                notes = change["notes"] as? String ?: "",
                improvements = change["improvements"] as? String ?: "",
                breakfastMenu = change["breakfastMenu"] as? String ?: "",
                lunchMenu = change["lunchMenu"] as? String ?: "",
                dinnerMenu = change["dinnerMenu"] as? String ?: "",
                timestamp = change["timestamp"] as? Long ?: System.currentTimeMillis()
            )
            if (entry.registerNumber.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.wellnessDao().insertWellnessEntry(entry) }
            }
        }

        // 6. Student Fees Sync
        safelyListen(fs, "student_fees") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val fee = StudentFee(
                id = id,
                studentRegister = change["studentRegister"] as? String ?: "",
                month = change["month"] as? String ?: "",
                year = (change["year"] as? Long)?.toInt() ?: 2026,
                amount = change["amount"] as? Double ?: 0.0,
                status = change["status"] as? String ?: "Pending",
                paymentDate = change["paymentDate"] as? String ?: "",
                paymentMode = change["paymentMode"] as? String ?: "",
                transactionReference = change["transactionReference"] as? String ?: "",
                remarks = change["remarks"] as? String ?: "",
                createdAt = change["createdAt"] as? Long ?: System.currentTimeMillis()
            )
            if (fee.studentRegister.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.studentFeeDao().insertFee(fee) }
            }
        }

        // 7. Coach Profiles Sync
        safelyListen(fs, "coach_profiles") { change ->
            val coach = CoachProfile(
                username = change["username"] as? String ?: "",
                name = change["name"] as? String ?: "",
                specialty = change["specialty"] as? String ?: "",
                academyName = change["academyName"] as? String ?: "",
                hasAccess = change["hasAccess"] as? Boolean ?: true
            )
            if (coach.username.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.coachDao().insertCoach(coach) }
            }
        }

        // 8. Tournaments Sync
        safelyListen(fs, "tournaments") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val tournament = Tournament(
                id = id,
                title = change["title"] as? String ?: "",
                date = change["date"] as? String ?: "",
                location = change["location"] as? String ?: "",
                academyName = change["academyName"] as? String ?: "",
                coachName = change["coachName"] as? String ?: "",
                timestamp = change["timestamp"] as? Long ?: System.currentTimeMillis()
            )
            if (tournament.title.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.tournamentDao().insertTournament(tournament) }
            }
        }

        // 9. Student Documents Sync
        safelyListen(fs, "student_documents") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val doc = StudentDocument(
                id = id,
                registerNumber = change["registerNumber"] as? String ?: "",
                documentName = change["documentName"] as? String ?: "",
                fileDetails = change["fileDetails"] as? String ?: "",
                status = change["status"] as? String ?: "Submitted",
                remarks = change["remarks"] as? String ?: "",
                timestamp = change["timestamp"] as? Long ?: System.currentTimeMillis()
            )
            if (doc.registerNumber.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.studentDocumentDao().insertDocument(doc) }
            }
        }

        // 10. Organizations Sync
        safelyListen(fs, "organizations") { change ->
            val id = (change["id"] as? Long)?.toInt() ?: 0
            val o = Organization(
                id = id,
                organizationName = change["organizationName"] as? String ?: "",
                contactPerson = change["contactPerson"] as? String ?: "",
                mobile = change["mobile"] as? String ?: "",
                email = change["email"] as? String ?: "",
                subscriptionPlan = change["subscriptionPlan"] as? String ?: "",
                activeStudentCount = (change["activeStudentCount"] as? Long)?.toInt() ?: 0,
                monthlyAmount = change["monthlyAmount"] as? Double ?: 0.0,
                subscriptionStartDate = change["subscriptionStartDate"] as? String ?: "",
                subscriptionEndDate = change["subscriptionEndDate"] as? String ?: "",
                status = change["status"] as? String ?: "Active"
            )
            if (o.organizationName.isNotBlank()) {
                scope.launch(Dispatchers.IO) { database.organizationDao().insertOrganization(o) }
            }
        }
    }

    private inline fun safelyListen(
        fs: FirebaseFirestore,
        collectionName: String,
        crossinline onDocChanged: (Map<String, Any>) -> Unit
    ) {
        fs.collection(collectionName).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("FirestoreSyncManager", "Error listening to collection $collectionName: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (doc in snapshot.documents) {
                    val data = doc.data
                    if (data != null) {
                        try {
                            onDocChanged(data)
                        } catch (e: Exception) {
                            Log.e("FirestoreSyncManager", "Failed to process doc ${doc.id} in $collectionName: ${e.message}")
                        }
                    }
                }
            }
        }
    }

    fun <T> uploadToCloud(collectionName: String, docId: String, data: T) {
        val fs = firestore ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fs.collection(collectionName).document(docId).set(data as Any, SetOptions.merge())
                Log.d("FirestoreSyncManager", "Synced item to Cloud: $collectionName/$docId")
            } catch (e: Exception) {
                Log.w("FirestoreSyncManager", "Cloud sync failure: ${e.message}")
            }
        }
    }

    fun deleteFromCloud(collectionName: String, docId: String) {
        val fs = firestore ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fs.collection(collectionName).document(docId).delete()
                Log.d("FirestoreSyncManager", "Deleted from Cloud: $collectionName/$docId")
            } catch (e: Exception) {
                Log.w("FirestoreSyncManager", "Cloud deletion failure: ${e.message}")
            }
        }
    }
}

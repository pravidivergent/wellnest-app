package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. Entities
@Entity(tableName = "student_profiles")
data class StudentProfile(
    @PrimaryKey val registerNumber: String,
    val name: String,
    val address: String,
    val mobileNumber: String,
    val parentMobile: String,
    val batch: String,
    val course: String,
    val profilePhoto: String // Name of avatar or representation
)

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val registerNumber: String,
    val date: String, // yyyy-MM-dd
    val shift: String, // "Morning" or "Evening"
    val status: String, // "Present", "Absent", "Late"
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "leave_applications")
data class LeaveApplication(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentRegister: String,
    val studentName: String,
    val startDate: String, // yyyy-MM-dd
    val endDate: String, // yyyy-MM-dd
    val reason: String,
    val proofName: String,
    val status: String, // "Pending", "Approved", "Rejected"
    val remarks: String = ""
)

@Entity(tableName = "wellness_entries")
data class WellnessEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val registerNumber: String,
    val date: String, // yyyy-MM-dd
    val sleepHours: Float,
    val hadBreakfast: Boolean,
    val hadLunch: Boolean,
    val hadDinner: Boolean,
    val waterIntakeCups: Int,
    val energyLevel: Int, // 1 to 10
    val mood: String, // "Happy", "Tired", "Stressed", "Calm", "Focused"
    val notes: String,
    val improvements: String,
    val timestamp: Long = System.currentTimeMillis()
)

// 2. DAOs
@Dao
interface StudentDao {
    @Query("SELECT * FROM student_profiles WHERE registerNumber = :regNo LIMIT 1")
    fun getStudentProfileFlow(regNo: String): Flow<StudentProfile?>

    @Query("SELECT * FROM student_profiles WHERE registerNumber = :regNo LIMIT 1")
    suspend fun getStudentProfileDirect(regNo: String): StudentProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudentProfile(student: StudentProfile)

    @Query("SELECT * FROM student_profiles")
    fun getAllStudentsFlow(): Flow<List<StudentProfile>>

    @Query("SELECT * FROM student_profiles")
    suspend fun getAllStudentsDirect(): List<StudentProfile>
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_records ORDER BY timestamp DESC")
    fun getAllAttendanceFlow(): Flow<List<AttendanceRecord>>

    @Query("SELECT * FROM attendance_records WHERE registerNumber = :regNo ORDER BY timestamp DESC")
    fun getStudentAttendanceFlow(regNo: String): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceRecord)
}

@Dao
interface LeaveDao {
    @Query("SELECT * FROM leave_applications ORDER BY id DESC")
    fun getAllLeavesFlow(): Flow<List<LeaveApplication>>

    @Query("SELECT * FROM leave_applications WHERE studentRegister = :regNo ORDER BY id DESC")
    fun getStudentLeavesFlow(regNo: String): Flow<List<LeaveApplication>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeave(leave: LeaveApplication)

    @Update
    suspend fun updateLeave(leave: LeaveApplication)
}

@Dao
interface WellnessDao {
    @Query("SELECT * FROM wellness_entries WHERE registerNumber = :regNo ORDER BY date DESC")
    fun getStudentWellnessEntriesFlow(regNo: String): Flow<List<WellnessEntry>>

    @Query("SELECT * FROM wellness_entries ORDER BY date DESC")
    fun getAllWellnessEntriesFlow(): Flow<List<WellnessEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWellnessEntry(entry: WellnessEntry)
}

// 3. Database
@Database(
    entities = [
        StudentProfile::class,
        AttendanceRecord::class,
        LeaveApplication::class,
        WellnessEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun leaveDao(): LeaveDao
    abstract fun wellnessDao(): WellnessDao
}

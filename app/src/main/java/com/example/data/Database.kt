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
    val profilePhoto: String, // Name of avatar or representation
    val academyName: String = "Springfield Academy"
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

@Entity(tableName = "student_fees")
data class StudentFee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val studentRegister: String,
    val month: String, // e.g. "January", "February", etc.
    val year: Int,
    val amount: Double,
    val status: String, // "Paid", "Pending", "Overdue"
    val paymentDate: String = "", // yyyy-MM-dd
    val paymentMode: String = "", // "Cash", "UPI", "Bank Transfer"
    val transactionReference: String = "",
    val remarks: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "organizations")
data class Organization(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val organizationName: String,
    val contactPerson: String,
    val mobile: String,
    val email: String,
    val subscriptionPlan: String = "Per Student",
    val activeStudentCount: Int = 0,
    val monthlyAmount: Double = 0.0,
    val subscriptionStartDate: String = "",
    val subscriptionEndDate: String = "",
    val status: String = "Active"
)

@Entity(tableName = "coach_profiles")
data class CoachProfile(
    @PrimaryKey val username: String, // Username/mobile
    val name: String,
    val specialty: String,
    val academyName: String,
    val hasAccess: Boolean = true
)

@Entity(tableName = "user_accounts")
data class UserAccount(
    @PrimaryKey val phoneNumber: String, // Username is the phone number
    val password: String,
    val role: String, // STUDENT, COACH, ADMIN
    val registerNumber: String = "", // Optional link for STUDENT
    val academyName: String = "",
    val hasAccess: Boolean = true
)

// 2. DAOs
@Dao
interface UserAccountDao {
    @Query("SELECT * FROM user_accounts WHERE phoneNumber = :phone LIMIT 1")
    suspend fun getAccountByPhoneDirect(phone: String): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: UserAccount)

    @Query("SELECT * FROM user_accounts")
    suspend fun getAllAccountsDirect(): List<UserAccount>
}

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

@Dao
interface StudentFeeDao {
    @Query("SELECT * FROM student_fees ORDER BY id DESC")
    fun getAllFeesFlow(): Flow<List<StudentFee>>

    @Query("SELECT * FROM student_fees WHERE studentRegister = :regNo ORDER BY id DESC")
    fun getStudentFeesFlow(regNo: String): Flow<List<StudentFee>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFee(fee: StudentFee)

    @Update
    suspend fun updateFee(fee: StudentFee)

    @Delete
    suspend fun deleteFee(fee: StudentFee)
}

@Dao
interface OrganizationDao {
    @Query("SELECT * FROM organizations ORDER BY id DESC")
    fun getOrganizationsFlow(): Flow<List<Organization>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrganization(org: Organization)

    @Update
    suspend fun updateOrganization(org: Organization)
}

@Dao
interface CoachDao {
    @Query("SELECT * FROM coach_profiles")
    fun getAllCoachesFlow(): Flow<List<CoachProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoach(coach: CoachProfile)

    @Update
    suspend fun updateCoach(coach: CoachProfile)

    @Delete
    suspend fun deleteCoach(coach: CoachProfile)
}

// 3. Database
@Database(
    entities = [
        StudentProfile::class,
        AttendanceRecord::class,
        LeaveApplication::class,
        WellnessEntry::class,
        StudentFee::class,
        Organization::class,
        UserAccount::class,
        CoachProfile::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun leaveDao(): LeaveDao
    abstract fun wellnessDao(): WellnessDao
    abstract fun studentFeeDao(): StudentFeeDao
    abstract fun organizationDao(): OrganizationDao
    abstract fun userAccountDao(): UserAccountDao
    abstract fun coachDao(): CoachDao
}

package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed interface AuthState {
    object Unauthenticated : AuthState
    data class OtpVerificationNeeded(
        val mobile: String,
        val registerNumber: String,
        val selectedRole: String
    ) : AuthState
    data class Authenticated(
        val role: String, // "STUDENT", "COACH", "ADMIN"
        val registerNumber: String, // Empty if not a Student
        val name: String,
        val mobile: String,
        val academyName: String = ""
    ) : AuthState
}

enum class AppLanguage(val code: String, val displayName: String) {
    EN("en", "English"),
    TA("ta", "தமிழ்")
}

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    // Authentication States
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Dark/Light Mode Setting State
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Language state
    private val _currentLanguage = MutableStateFlow(AppLanguage.EN)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    // Active User Student Profile (if active user is STUDENT)
    private val _currentStudentProfile = MutableStateFlow<StudentProfile?>(null)
    val currentStudentProfile: StateFlow<StudentProfile?> = _currentStudentProfile.asStateFlow()

    // Flow listings from repository
    val allStudents = repository.allStudentsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allCoaches = repository.allCoachesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAttendance = repository.allAttendanceFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allLeaves = repository.allLeavesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allWellness = repository.allWellnessEntriesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allFees = repository.allFeesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allOrganizations = repository.allOrganizationsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Automatically seed mock database entries if empty so the screen experiences rich initial reports
        viewModelScope.launch {
            seedInitialRecordsIfEmpty()
        }
    }

    private suspend fun seedInitialRecordsIfEmpty() {
        val existing = repository.getAllStudentsDirect()
        if (existing.isNotEmpty()) return

        // 1. Seed Student Profiles
        val stu1 = StudentProfile(
            registerNumber = "2026CS501",
            name = "Alex Johnson",
            address = "742 Evergreen Terrace, Springfield",
            mobileNumber = "9876543210",
            parentMobile = "9123456780",
            batch = "Batch CS-A",
            course = "Computer Science & Eng",
            profilePhoto = "avatar_1",
            academyName = "Springfield Academy"
        )
        val stu2 = StudentProfile(
            registerNumber = "2026CS502",
            name = "Siddharth Verma",
            address = "456 Silver Oak Ridge, Chicago",
            mobileNumber = "9876543211",
            parentMobile = "9123456781",
            batch = "Batch CS-A",
            course = "Information Technology",
            profilePhoto = "avatar_2",
            academyName = "Springfield Academy"
        )
        val stu3 = StudentProfile(
            registerNumber = "2025EC408",
            name = "Cynthia Smith",
            address = "89 Rosewood Circle, Austin",
            mobileNumber = "9876543212",
            parentMobile = "9123456782",
            batch = "Batch EC-B",
            course = "Electronics & Comm Eng",
            profilePhoto = "avatar_3",
            academyName = "Stamford Academy"
        )

        repository.insertStudentProfile(stu1)
        repository.insertStudentProfile(stu2)
        repository.insertStudentProfile(stu3)

        // 2. Seed Attendance Records
        val days = listOf("2026-05-25", "2026-05-26", "2026-05-27", "2026-05-28", "2026-05-29")
        for (day in days) {
            // Alex: Mostly Present
            repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS501", date = day, shift = "Morning", status = "Present"))
            repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS501", date = day, shift = "Evening", status = "Present"))

            // Siddharth: Late some days, Absent on 28th
            if (day == "2026-05-28") {
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Morning", status = "Absent"))
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Evening", status = "Absent"))
            } else if (day == "2026-05-26") {
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Morning", status = "Late"))
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Evening", status = "Present"))
            } else {
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Morning", status = "Present"))
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Evening", status = "Present"))
            }

            // Cynthia: All present
            repository.insertAttendance(AttendanceRecord(registerNumber = "2025EC408", date = day, shift = "Morning", status = "Present"))
            repository.insertAttendance(AttendanceRecord(registerNumber = "2025EC408", date = day, shift = "Evening", status = "Present"))
        }

        // 3. Seed Leave requests
        repository.insertLeave(
            LeaveApplication(
                studentRegister = "2026CS502",
                studentName = "Siddharth Verma",
                startDate = "2026-06-02",
                endDate = "2026-06-04",
                reason = "Family wedding out of town",
                proofName = "wedding_invitation.pdf",
                status = "Pending"
            )
        )
        repository.insertLeave(
            LeaveApplication(
                studentRegister = "2025EC408",
                studentName = "Cynthia Smith",
                startDate = "2026-05-20",
                endDate = "2026-05-21",
                reason = "Dental wisdom teeth surgery extraction",
                proofName = "medical_certificate.jpg",
                status = "Approved",
                remarks = "Approved. Take rest and submit class work on return."
            )
        )

        // 4. Seed Wellness entries
        repository.insertWellnessEntry(
            WellnessEntry(
                registerNumber = "2026CS501",
                date = "2026-05-28",
                sleepHours = 7.5f,
                hadBreakfast = true,
                hadLunch = true,
                hadDinner = true,
                waterIntakeCups = 8,
                energyLevel = 8,
                mood = "Calm",
                notes = "Felt active, completed assignments.",
                improvements = "Will sleep slightly earlier."
            )
        )
        repository.insertWellnessEntry(
            WellnessEntry(
                registerNumber = "2026CS502",
                date = "2026-05-28",
                sleepHours = 4.5f,
                hadBreakfast = false,
                hadLunch = true,
                hadDinner = false,
                waterIntakeCups = 3,
                energyLevel = 3,
                mood = "Tired",
                notes = "Slept very late preparing for coding exam, skipped lunch/dinner routine.",
                improvements = "Must maintain meal times."
            )
        )

        // 5. Seed Student Fees
        repository.insertFee(
            StudentFee(
                studentRegister = "2026CS501",
                month = "May",
                year = 2026,
                amount = 1500.0,
                status = "Paid",
                paymentDate = "2026-05-15",
                paymentMode = "UPI",
                transactionReference = "TXN123456789",
                remarks = "Thank you for early payment"
            )
        )
        repository.insertFee(
            StudentFee(
                studentRegister = "2026CS502",
                month = "May",
                year = 2026,
                amount = 1500.0,
                status = "Pending",
                remarks = "Reminder sent via parents"
            )
        )
        repository.insertFee(
            StudentFee(
                studentRegister = "2025EC408",
                month = "May",
                year = 2026,
                amount = 1500.0,
                status = "Overdue",
                remarks = "More than 15 days late"
            )
        )

        // 6. Seed Organizations
        repository.insertOrganization(
            Organization(
                organizationName = "Springfield Academy",
                contactPerson = "Principal Skinner",
                mobile = "9876543210",
                email = "skinner@springfield.edu",
                subscriptionPlan = "Per Student (₹100/mo)",
                activeStudentCount = 2, // Springfield has Alex & Siddharth
                monthlyAmount = 200.0,
                subscriptionStartDate = "2026-01-01",
                subscriptionEndDate = "2026-12-31",
                status = "Active"
            )
        )
        repository.insertOrganization(
            Organization(
                organizationName = "Stamford Academy",
                contactPerson = "Principal Stamford",
                mobile = "9876543212",
                email = "stamford@academy.edu",
                subscriptionPlan = "Per Student (₹100/mo)",
                activeStudentCount = 1, // Stamford has Cynthia
                monthlyAmount = 100.0,
                subscriptionStartDate = "2026-01-01",
                subscriptionEndDate = "2026-12-31",
                status = "Active"
            )
        )

        // 7. Seed Default User Accounts for Phone & Password Login
        repository.insertUserAccount(
            UserAccount(phoneNumber = "9876543210", password = "password123", role = "STUDENT", registerNumber = "2026CS501", academyName = "Springfield Academy")
        )
        repository.insertUserAccount(
            UserAccount(phoneNumber = "9876543211", password = "password123", role = "STUDENT", registerNumber = "2026CS502", academyName = "Springfield Academy")
        )
        repository.insertUserAccount(
            UserAccount(phoneNumber = "9876543212", password = "password123", role = "STUDENT", registerNumber = "2025EC408", academyName = "Stamford Academy")
        )
        repository.insertUserAccount(
            UserAccount(phoneNumber = "9900990099", password = "password123", role = "COACH", academyName = "Springfield Academy")
        )
        repository.insertUserAccount(
            UserAccount(phoneNumber = "8888888888", password = "password123", role = "ADMIN", academyName = "Springfield Academy")
        )
        repository.insertUserAccount(
            UserAccount(phoneNumber = "7777777777", password = "password123", role = "ADMIN", academyName = "Stamford Academy")
        )

        // Seed Coach Profiles
        repository.insertCoach(
            CoachProfile(username = "9900990099", name = "Coach Harrison", specialty = "Soccer Chief Coach", academyName = "Springfield Academy", hasAccess = true)
        )
        repository.insertCoach(
            CoachProfile(username = "9911991199", name = "Coach Stamford", specialty = "Tennis & Swimming", academyName = "Stamford Academy", hasAccess = true)
        )
    }

    fun recordFeePayment(
        studentRegister: String,
        month: String,
        year: Int,
        amount: Double,
        status: String,
        paymentDate: String = "",
        paymentMode: String = "",
        transactionReference: String = "",
        remarks: String = ""
    ) {
        viewModelScope.launch {
            repository.insertFee(
                StudentFee(
                    studentRegister = studentRegister,
                    month = month,
                    year = year,
                    amount = amount,
                    status = status,
                    paymentDate = paymentDate,
                    paymentMode = paymentMode,
                    transactionReference = transactionReference,
                    remarks = remarks
                )
            )
        }
    }

    fun updateFeeStatus(fee: StudentFee) {
        viewModelScope.launch {
            repository.updateFee(fee)
        }
    }

    fun addOrganization(
        name: String,
        contactPerson: String,
        mobile: String,
        email: String,
        activeStudentCount: Int,
        subscriptionStartDate: String,
        subscriptionEndDate: String,
        status: String = "Active"
    ) {
        viewModelScope.launch {
            val amount = activeStudentCount * 100.0
            repository.insertOrganization(
                Organization(
                    organizationName = name,
                    contactPerson = contactPerson,
                    mobile = mobile,
                    email = email,
                    subscriptionPlan = "Per Student (₹100/mo)",
                    activeStudentCount = activeStudentCount,
                    monthlyAmount = amount,
                    subscriptionStartDate = subscriptionStartDate,
                    subscriptionEndDate = subscriptionEndDate,
                    status = status
                )
            )
        }
    }

    fun updateOrganizationDetails(org: Organization) {
        viewModelScope.launch {
            repository.updateOrganization(org)
        }
    }

    // Submit Log / Auth Routines
    fun requestOtp(mobile: String, role: String, regNo: String) {
        if (mobile.isBlank()) return
        // In Student flow, verify registration is completed or use placeholder student
        val actualRegNo = if (role == "STUDENT") {
            if (regNo.isBlank()) "2026CS501" else regNo
        } else {
            ""
        }
        _authState.value = AuthState.OtpVerificationNeeded(
            mobile = mobile,
            registerNumber = actualRegNo,
            selectedRole = role
        )
    }

    fun verifyOtpAndLogin(code: String): Boolean {
        val current = _authState.value
        if (current is AuthState.OtpVerificationNeeded) {
            // Any 6 digits will succeed for this production-grade simulation
            if (code.length == 6) {
                viewModelScope.launch {
                    val name = when (current.selectedRole) {
                        "COACH" -> "Coach Harrison"
                        "ADMIN" -> "Admin Controller"
                        else -> {
                            val profile = repository.getStudentProfileDirect(current.registerNumber)
                            if (profile == null) {
                                // Instantly create a new student profile if they logged in with new registration
                                val newProfile = StudentProfile(
                                    registerNumber = current.registerNumber,
                                    name = "New Student (" + current.registerNumber + ")",
                                    address = "Springfield",
                                    mobileNumber = current.mobile,
                                    parentMobile = "9123451234",
                                    batch = "Batch CS-A",
                                    course = "Computer Science",
                                    profilePhoto = "avatar_1"
                                )
                                repository.insertStudentProfile(newProfile)
                                _currentStudentProfile.value = newProfile
                                newProfile.name
                            } else {
                                _currentStudentProfile.value = profile
                                profile.name
                            }
                        }
                    }
                    _authState.value = AuthState.Authenticated(
                        role = current.selectedRole,
                        registerNumber = current.registerNumber,
                        name = name,
                        mobile = current.mobile
                    )
                }
                return true
            }
        }
        return false
    }

    fun backToLogin() {
        _authState.value = AuthState.Unauthenticated
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
        _currentStudentProfile.value = null
    }

    // Student profile edit
    fun saveStudentProfile(updatedProfile: StudentProfile) {
        viewModelScope.launch {
            repository.insertStudentProfile(updatedProfile)
            _currentStudentProfile.value = updatedProfile
        }
    }

    // Double-check is registered
    fun setStudentDirectly(regNo: String) {
        viewModelScope.launch {
            val prof = repository.getStudentProfileDirect(regNo)
            if (prof != null) {
                _currentStudentProfile.value = prof
            }
        }
    }

    // Insert attendance
    fun studentCheckIn(registerNumber: String, shift: String, status: String) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            repository.insertAttendance(
                AttendanceRecord(
                    registerNumber = registerNumber,
                    date = today,
                    shift = shift,
                    status = status
                )
            )
        }
    }

    // Submit Leave Application
    fun applyForLeave(registerNumber: String, name: String, startStr: String, endStr: String, reason: String, proof: String) {
        viewModelScope.launch {
            repository.insertLeave(
                LeaveApplication(
                    studentRegister = registerNumber,
                    studentName = name,
                    startDate = startStr,
                    endDate = endStr,
                    reason = reason,
                    proofName = proof,
                    status = "Pending"
                )
            )
        }
    }

    // Submit Daily Wellness Entry
    fun submitWellness(
        registerNumber: String,
        sleepHours: Float,
        brekkie: Boolean,
        lunch: Boolean,
        dinner: Boolean,
        waterCups: Int,
        energy: Int,
        mood: String,
        notes: String,
        improvements: String
    ) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            repository.insertWellnessEntry(
                WellnessEntry(
                    registerNumber = registerNumber,
                    date = today,
                    sleepHours = sleepHours,
                    hadBreakfast = brekkie,
                    hadLunch = lunch,
                    hadDinner = dinner,
                    waterIntakeCups = waterCups,
                    energyLevel = energy,
                    mood = mood,
                    notes = notes,
                    improvements = improvements
                )
            )
        }
    }

    // Coach updates leave status
    fun coachApproveLeave(leaveId: Int, isApproved: Boolean, remarks: String) {
        viewModelScope.launch {
            val findLeave = allLeaves.value.find { it.id == leaveId }
            if (findLeave != null) {
                val updated = findLeave.copy(
                    status = if (isApproved) "Approved" else "Rejected",
                    remarks = remarks
                )
                repository.updateLeave(updated)
            }
        }
    }

    // New Signup Flow
    fun createOneTimeAccount(phone: String, pass: String, role: String, regNo: String = "", academyName: String = "", onResult: (Boolean, String) -> Unit) {
        if (phone.isBlank() || pass.isBlank()) {
            onResult(false, "Phone and password cannot be empty.")
            return
        }
        viewModelScope.launch {
            val existing = repository.getAccountByPhone(phone)
            if (existing != null) {
                onResult(false, "Username (Phone number) already exists.")
            } else {
                repository.insertUserAccount(
                    UserAccount(phoneNumber = phone, password = pass, role = role, registerNumber = if (role == "STUDENT") regNo else "", academyName = academyName)
                )
                if (role == "STUDENT") {
                    repository.insertStudentProfile(
                        StudentProfile(
                            registerNumber = regNo,
                            name = "Student ($phone)",
                            address = "General",
                            mobileNumber = phone,
                            parentMobile = "9123456789",
                            batch = "Batch CS-A",
                            course = "Computer Science",
                            profilePhoto = "avatar_1",
                            academyName = academyName
                        )
                    )
                }
                onResult(true, "Account created successfully! Please login with your credentials.")
            }
        }
    }

    fun addCoachDetails(name: String, username: String, pass: String, specialty: String, academy: String, hasAccess: Boolean) {
        viewModelScope.launch {
            repository.insertCoach(
                CoachProfile(username = username, name = name, specialty = specialty, academyName = academy, hasAccess = hasAccess)
            )
            repository.insertUserAccount(
                UserAccount(phoneNumber = username, password = pass, role = "COACH", academyName = academy, hasAccess = hasAccess)
            )
        }
    }

    fun updateCoachAccess(coach: CoachProfile, hasAccess: Boolean) {
        viewModelScope.launch {
            val updatedCoach = coach.copy(hasAccess = hasAccess)
            repository.insertCoach(updatedCoach)
            val account = repository.getAccountByPhone(coach.username)
            if (account != null) {
                repository.insertUserAccount(account.copy(hasAccess = hasAccess))
            }
        }
    }

    fun forceSwitchRole(newRole: String) {
        val current = _authState.value
        if (current is AuthState.Authenticated) {
            _authState.value = current.copy(role = newRole)
        }
    }

    // New Login Flow with Phone and Password
    fun loginWithPassword(phone: String, pass: String, role: String = "", regNo: String = "", academy: String = "", onResult: (Boolean, String) -> Unit) {
        if (phone.isBlank() || pass.isBlank()) {
            onResult(false, "Phone and password cannot be empty.")
            return
        }
        viewModelScope.launch {
            val account = repository.getAccountByPhone(phone)
            if (account == null) {
                onResult(false, "No account found with this username (phone number).")
            } else if (account.password != pass) {
                onResult(false, "Incorrect password.")
            } else if (account.role == "COACH" && academy.isNotBlank() && !account.academyName.equals(academy, ignoreCase = true)) {
                onResult(false, "Access Denied: You are not registered at $academy.")
            } else if (account.role == "COACH" && !account.hasAccess) {
                onResult(false, "Access Denied: Your access is turned off by Academy Admin.")
            } else {
                // If role argument is passed as blank, auto-resolve it from the database account!
                val resolvedRole = if (role.isNotBlank()) role else account.role
                val userAcademy = if (account.academyName.isNotBlank()) account.academyName else "Springfield Academy"
                
                val studentReg = if (resolvedRole == "STUDENT") {
                    if (account.registerNumber.isNotEmpty()) account.registerNumber else regNo
                } else {
                    ""
                }
                
                val name = when (resolvedRole) {
                    "COACH" -> {
                        val coachRecord = repository.allCoachesFlow.firstOrNull()?.find { it.username == phone }
                        coachRecord?.name ?: "Coach Harrison"
                    }
                    "ADMIN" -> "Admin Controller"
                    else -> {
                        val profile = repository.getStudentProfileDirect(studentReg)
                        if (profile == null) {
                            val newProfile = StudentProfile(
                                registerNumber = studentReg,
                                name = "Alex Johnson",
                                address = "Springfield",
                                mobileNumber = phone,
                                parentMobile = "9123456780",
                                batch = "Batch CS-A",
                                course = "Computer Science",
                                profilePhoto = "avatar_1",
                                academyName = userAcademy
                            )
                            repository.insertStudentProfile(newProfile)
                            _currentStudentProfile.value = newProfile
                            newProfile.name
                        } else {
                            _currentStudentProfile.value = profile
                            profile.name
                        }
                    }
                }
                
                // Get the final student profile and use its actual academy to keep synced
                val finalAcademy = if (resolvedRole == "STUDENT") {
                    _currentStudentProfile.value?.academyName ?: userAcademy
                } else {
                    userAcademy
                }
                
                _authState.value = AuthState.Authenticated(
                    role = resolvedRole,
                    registerNumber = studentReg,
                    name = name,
                    mobile = phone,
                    academyName = finalAcademy
                )
                onResult(true, "Logged in as $name ($resolvedRole)")
            }
        }
    }
}

class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

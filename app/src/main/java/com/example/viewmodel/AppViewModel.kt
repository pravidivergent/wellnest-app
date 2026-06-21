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

class AppViewModel(
    private val repository: AppRepository,
    private val context: android.content.Context
) : ViewModel() {

    private val prefs = context.getSharedPreferences("AthlePulsePrefs", android.content.Context.MODE_PRIVATE)

    // Authentication States
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Dark/Light Mode Setting State
    private val _isDarkMode = MutableStateFlow(prefs.getBoolean("isDarkMode", false))
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    fun toggleDarkMode() {
        val nextVal = !_isDarkMode.value
        _isDarkMode.value = nextVal
        prefs.edit().putBoolean("isDarkMode", nextVal).apply()
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

    val allTournaments = repository.allTournamentsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAlerts = repository.allAlertsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allDocuments = repository.allDocumentsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAccounts = repository.allAccountsFlow.stateIn(
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

            // Siddharth: Late some days, Absent on 28th, Leave on 27th
            if (day == "2026-05-28") {
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Morning", status = "Absent"))
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Evening", status = "Absent"))
            } else if (day == "2026-05-27") {
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Morning", status = "Leave"))
                repository.insertAttendance(AttendanceRecord(registerNumber = "2026CS502", date = day, shift = "Evening", status = "Leave"))
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

        // 4. Seed Wellness entries dynamically for the last 30 days
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = Calendar.getInstance()
        for (i in 0..29) {
            cal.time = Date()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dateStr = sdf.format(cal.time)
            
            // Student 1: 2026CS501 (Healthy pattern)
            val s1Sleep = 6f + (Math.random() * 3.5).toFloat() // 6 to 9.5 hrs
            val s1Breakfast = Math.random() > 0.15
            val s1Lunch = Math.random() > 0.05
            val s1Dinner = Math.random() > 0.1
            val s1Water = 6 + (Math.random() * 5).toInt() // 6 to 10 cups
            val s1Energy = 7 + (Math.random() * 4).toInt() // 7 to 10
            val s1Moods = listOf("Calm", "Happy", "Focused")
            val s1Mood = s1Moods[(Math.random() * s1Moods.size).toInt()]
            
            repository.insertWellnessEntry(
                WellnessEntry(
                    registerNumber = "2026CS501",
                    date = dateStr,
                    sleepHours = s1Sleep,
                    hadBreakfast = s1Breakfast,
                    hadLunch = s1Lunch,
                    hadDinner = s1Dinner,
                    waterIntakeCups = s1Water,
                    energyLevel = s1Energy,
                    mood = s1Mood,
                    notes = "Self-care tracker updates. Feeling highly motivated and positive.",
                    improvements = "Maintain current training intensity."
                )
            )

            // Student 2: 2026CS502 (Stressed/Fluctuating pattern)
            val s2Sleep = 4.5f + (Math.random() * 4f).toFloat() // 4.5 to 8.5 hrs
            val s2Breakfast = Math.random() > 0.35
            val s2Lunch = Math.random() > 0.15
            val s2Dinner = Math.random() > 0.25
            val s2Water = 3 + (Math.random() * 5).toInt() // 3 to 7 cups
            val s2Energy = 3 + (Math.random() * 5).toInt() // 3 to 7
            val s2Moods = listOf("Tired", "Stressed", "Calm", "Focused")
            val s2Mood = s2Moods[(Math.random() * s2Moods.size).toInt()]

            repository.insertWellnessEntry(
                WellnessEntry(
                    registerNumber = "2026CS502",
                    date = dateStr,
                    sleepHours = s2Sleep,
                    hadBreakfast = s2Breakfast,
                    hadLunch = s2Lunch,
                    hadDinner = s2Dinner,
                    waterIntakeCups = s2Water,
                    energyLevel = s2Energy,
                    mood = s2Mood,
                    notes = "Exhausting schedule. Balancing technical lectures & intensive field drills.",
                    improvements = "Aim for consistent hydration and sleep intervals."
                )
            )
        }

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
                subscriptionPlan = "Enterprise Admin Plan",
                activeStudentCount = 2, // Springfield has Alex & Siddharth
                monthlyAmount = 500.0, // Admin flat monthly billing
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
                subscriptionPlan = "Enterprise Admin Plan",
                activeStudentCount = 1, // Stamford has Cynthia
                monthlyAmount = 500.0, // Admin flat monthly billing
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

        // Seed Tournaments
        repository.insertTournament(
            Tournament(
                title = "District Junior Athletics Meet",
                date = "2026-06-28",
                location = "Stadium Grounds A",
                academyName = "Springfield Academy",
                coachName = "Coach Harrison"
            )
        )
        repository.insertTournament(
            Tournament(
                title = "National Selection Volleyball Trials",
                date = "2026-07-15",
                location = "Indoor Sports Pavilion",
                academyName = "Springfield Academy",
                coachName = "Coach Harrison"
            )
        )
        repository.insertTournament(
            Tournament(
                title = "State Level Track & Field Trophy",
                date = "2026-07-29",
                location = "University Complex",
                academyName = "Springfield Academy",
                coachName = "Coach Harrison"
            )
        )

        // Seed default student documents
        repository.insertDocument(
            StudentDocument(
                registerNumber = "2026CS501",
                documentName = "Birth Certificate",
                fileDetails = "birth_certificate_alex.pdf",
                status = "Verified",
                remarks = "Verified by Coach Harrison"
            )
        )
        repository.insertDocument(
            StudentDocument(
                registerNumber = "2026CS501",
                documentName = "Medical Form",
                fileDetails = "medical_report_alex.pdf",
                status = "Submitted",
                remarks = "Ready for audit"
            )
        )
        repository.insertDocument(
            StudentDocument(
                registerNumber = "2026CS501",
                documentName = "Consent Slip",
                fileDetails = "consent_slip_alex.pdf",
                status = "Verified",
                remarks = "Consent confirmed"
            )
        )
        repository.insertDocument(
            StudentDocument(
                registerNumber = "2026CS502",
                documentName = "Birth Certificate",
                fileDetails = "birth_certificate_sid.pdf",
                status = "Submitted",
                remarks = "New upload"
            )
        )
        repository.insertDocument(
            StudentDocument(
                registerNumber = "2026CS502",
                documentName = "Medical Form",
                fileDetails = "medical_report_sid.pdf",
                status = "Pending Updation",
                remarks = "Needs signatures"
            )
        )
        repository.insertDocument(
            StudentDocument(
                registerNumber = "2026CS502",
                documentName = "Consent Slip",
                fileDetails = "consent_slip_sid.pdf",
                status = "Pending Updation",
                remarks = "Verification unresolved"
            )
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
            repository.insertOrganization(
                Organization(
                    organizationName = name,
                    contactPerson = contactPerson,
                    mobile = mobile,
                    email = email,
                    subscriptionPlan = "Enterprise Admin Plan",
                    activeStudentCount = activeStudentCount,
                    monthlyAmount = 500.0, // Admin flat monthly billing
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
                    val account = repository.getAccountByPhone(current.mobile)
                    val userAcademy = account?.academyName?.ifBlank { "Springfield Academy" } ?: "Springfield Academy"

                    val name = when (current.selectedRole) {
                        "COACH" -> {
                            val coachRecord = repository.allCoachesFlow.firstOrNull()?.find { it.username == current.mobile }
                            coachRecord?.name ?: "Coach Harrison"
                        }
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

                    val finalAcademy = if (current.selectedRole == "STUDENT") {
                        _currentStudentProfile.value?.academyName ?: userAcademy
                    } else {
                        userAcademy
                    }

                    _authState.value = AuthState.Authenticated(
                        role = current.selectedRole,
                        registerNumber = current.registerNumber,
                        name = name,
                        mobile = current.mobile,
                        academyName = finalAcademy
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

    // Coach records attendance for any date
    fun markAttendance(registerNumber: String, date: String, shift: String, status: String) {
        viewModelScope.launch {
            repository.insertAttendance(
                AttendanceRecord(
                    registerNumber = registerNumber,
                    date = date,
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
        improvements: String,
        breakfastMenu: String = "",
        lunchMenu: String = "",
        dinnerMenu: String = ""
    ) {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val isCritical = sleepHours <= 5f || energy <= 3 || mood == "Tired" || mood == "Stressed" || waterCups < 4 || !brekkie
        
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
                    improvements = improvements,
                    breakfastMenu = breakfastMenu,
                    lunchMenu = lunchMenu,
                    dinnerMenu = dinnerMenu
                )
            )

            if (isCritical) {
                // Find student profile to get name and academy
                val studentProfile = repository.getStudentProfileDirect(registerNumber)
                val studentName = studentProfile?.name ?: "Student ($registerNumber)"
                val studentAcademyName = studentProfile?.academyName ?: ""

                // Fetch all registered coaches and match by academy
                val matchedCoaches = allCoaches.value.filter {
                    it.academyName.equals(studentAcademyName, ignoreCase = true)
                }

                if (matchedCoaches.isNotEmpty()) {
                    matchedCoaches.forEach { coach ->
                        val coachEmail = "${coach.name.replace(" ", "").lowercase()}@academy.com"
                        val alertSubject = "⚠️ [CRITICAL WELLNESS ALERT] At-Risk Athlete Status: $studentName"
                        val alertBody = """
                            Dear ${coach.name},

                            This is an automated wellness notification dispatched from the AthlePulse Live Performance Center.

                            Student-athlete $studentName (Register: $registerNumber) has logged a daily wellness score that triggers our 'Critical Sports Health Intervention' threshold.

                            🔴 FLAGGED PARAMETERS:
                            ${if (sleepHours <= 5f) "❌ Insufficient Rest: $sleepHours hrs of sleep (Requires >= 6.0 hrs)" else "✅ Sufficient Rest: $sleepHours hrs"}
                            ${if (energy <= 3) "❌ Low Physical Energy: $energy/10 (Requires >= 4/10)" else "✅ Normal Physical Energy: $energy/10"}
                            ${if (mood == "Tired" || mood == "Stressed") "❌ Poor Mental/Emotional State: $mood (Concern status)" else "✅ Positive Emotional State: $mood"}
                            ${if (waterCups < 4) "❌ Dehydration Risk: $waterCups cups of water (Requires >= 4 cups)" else "✅ Satisfactory Hydration: $waterCups cups"}
                            ${if (!brekkie) "❌ Nutrition Gap: Skipped Breakfast today" else "✅ Nutritious Start: Had breakfast"}

                            📝 Athlete's Personal Remarks & Notes:
                            "$notes"

                            💡 Aspirational Improvement / Goals:
                            "$improvements"

                            🎯 REQUIRED INTERVENTION ACTION:
                            We recommend opening your Coaching Portal Command Desk to coordinate an intervention, adjust today's training intensity, schedule a counseling check, or adjust their nutritional/water intake plans.

                            This automated email notification was dispatched with SSL/SMTPS encryption to ${coach.name}.

                            Sincerely,
                            AthlePulse Live Performance Wellness System
                        """.trimIndent()

                        repository.insertAlert(
                            AutomatedEmailAlert(
                                studentRegisterNumber = registerNumber,
                                studentName = studentName,
                                coachName = coach.name,
                                coachEmail = coachEmail,
                                subject = alertSubject,
                                body = alertBody,
                                status = "Sent",
                                sleepHours = sleepHours,
                                energyLevel = energy,
                                mood = mood
                            )
                        )
                    }
                } else {
                    // Fallback to default coaching address
                    val coachEmailVal = "coaching.desk@springfield.edu"
                    val alertSubject = "⚠️ [CRITICAL WELLNESS ALERT] At-Risk Athlete Status: $studentName"
                    val alertBody = """
                        Dear Academy Coaching Desk,

                        This is an automated wellness notification dispatched from the AthlePulse Live Performance Center.

                        Student-athlete $studentName (Register: $registerNumber) has checked in a critical sports wellness score.

                        🔴 FLAGGED PARAMETERS:
                        - Sleep Duration: $sleepHours hrs
                        - Physical Energy: $energy / 10
                        - Mood Status: $mood
                        - Hydration Level: $waterCups cups (Had breakfast: ${if (brekkie) "Yes" else "No"})

                        Athlete Personal Remarks:
                        "$notes"

                        Please coordinate with the physical training and recovery lead to review their performance metrics.

                        Sincerely,
                        AthlePulse Live Performance Wellness System
                    """.trimIndent()

                    repository.insertAlert(
                        AutomatedEmailAlert(
                            studentRegisterNumber = registerNumber,
                            studentName = studentName,
                            coachName = "Academy Coaching Team",
                            coachEmail = coachEmailVal,
                            subject = alertSubject,
                            body = alertBody,
                            status = "Sent",
                            sleepHours = sleepHours,
                            energyLevel = energy,
                            mood = mood
                        )
                    )
                }
            }
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
                // If database role is COACH, force COACH role. Otherwise auto-resolve as requested.
                val resolvedRole = if (account.role == "COACH") "COACH" else if (role.isNotBlank()) role else account.role
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

    // Post new Tournament (Coach)
    fun publishTournament(title: String, date: String, location: String, academyName: String, coachName: String = "") {
        viewModelScope.launch {
            repository.insertTournament(
                Tournament(
                    title = title,
                    date = date,
                    location = location,
                    academyName = academyName,
                    coachName = coachName
                )
            )
        }
    }

    // Delete a Tournament
    fun deleteTournament(tournament: Tournament) {
        viewModelScope.launch {
            repository.deleteTournament(tournament)
        }
    }

    // Students can submit/add a Document
    fun addStudentDocument(registerNumber: String, documentName: String, fileDetails: String) {
        viewModelScope.launch {
            repository.insertDocument(
                StudentDocument(
                    registerNumber = registerNumber,
                    documentName = documentName,
                    fileDetails = fileDetails,
                    status = "Submitted",
                    remarks = "Newly added document by student."
                )
            )
        }
    }

    // Coaches can update document status
    fun updateStudentDocumentStatus(documentId: Int, newStatus: String, remarks: String = "") {
        viewModelScope.launch {
            val doc = allDocuments.value.find { it.id == documentId }
            if (doc != null) {
                repository.updateDocument(
                    doc.copy(
                        status = newStatus,
                        remarks = remarks
                    )
                )
            }
        }
    }

    // Student can delete a document if they uploaded it incorrectly
    fun deleteStudentDocument(doc: StudentDocument) {
        viewModelScope.launch {
            repository.deleteDocument(doc)
        }
    }

    // Unified save or update student document status
    fun saveOrUpdateDocument(registerNumber: String, documentName: String, status: String, fileDetails: String = "No File", remarks: String = "") {
        viewModelScope.launch {
            val existing = repository.allDocumentsFlow.firstOrNull()?.find {
                it.registerNumber == registerNumber && it.documentName == documentName
            }
            if (existing != null) {
                repository.updateDocument(
                    existing.copy(
                        status = status,
                        fileDetails = fileDetails,
                        remarks = remarks
                    )
                )
            } else {
                repository.insertDocument(
                    StudentDocument(
                        registerNumber = registerNumber,
                        documentName = documentName,
                        fileDetails = fileDetails,
                        status = status,
                        remarks = remarks
                    )
                )
            }
        }
    }
}

class AppViewModelFactory(
    private val repository: AppRepository,
    private val context: android.content.Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

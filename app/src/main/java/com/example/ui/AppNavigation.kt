package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.viewmodel.AuthState
import com.example.viewmodel.AppViewModel
import com.example.viewmodel.AppLanguage
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

val translations = mapOf(
    AppLanguage.EN to mapOf(
        "wellnest_title" to "Wellnest",
        "wellnest_subtitle" to "Secure Student Daily Wellness & Attendance Analytics",
        "switch_role_title" to "Switch Portal Role",
        "switch_role_desc" to "Select which dashboard/role you would like to test or switch into:",
        "switch_lang_title" to "Choose Language",
        "switch_lang_desc" to "Select system-wide display language:",
        "student_portal" to "🎓 Student Portal",
        "coach_portal" to "⚽ Coach / Staff Portal",
        "admin_portal" to "Central Admin Portal",
        "cancel" to "Cancel",
        "logout" to "Log out",
        "exit" to "Exit",
        "wellness_title" to "Daily Wellness Tracker",
        "wellness_subtitle" to "Record sleep hours, meals count, energy level, and feelings to generate a healthy diagnostic report.",
        "sleep_hours_count" to "🛌 Sleep Hours Count",
        "meals_question" to "🍽️ Did you have meals today?",
        "water_intake" to "💧 Daily Water Intake (Liters)",
        "mental_vibe" to "🎭 Overall Mental Vibe / Mood",
        "wellness_notes" to "📝 Mental Wellness Notes",
        "wellness_goals" to "🔮 Health & Wellness Goals/Improvements",
        "wellness_placeholder" to "What made you feel like this today? Describe challenges.",
        "goals_placeholder" to "What goals do you have for tomorrow?",
        "success_saved" to "🎉 Daily wellness diagnostics saved successfully!",
        "btn_log_wellness" to "Securely Log Wellness",
        "leave_title" to "Leave Management",
        "leave_subtitle" to "Request formal absences. Upload verification proofs for Coach reviews.",
        "leave_applicant" to "📄 APPLICANT",
        "start_date" to "Start Date (yyyy-MM-dd)",
        "end_date" to "End Date (yyyy-MM-dd)",
        "absence_reason" to "Absence Reason",
        "reason_placeholder" to "Details or medical surgery reasons",
        "proof_attachment" to "Verification Proof attachment (.jpg/.pdf)",
        "proof_placeholder" to "e.g. prescription_receipt.pdf",
        "leave_success_msg" to "Submitted to your assigned Coach dashboard. Monitor decision report below.",
        "btn_submit_leave" to "Submit Absence Request",
        "absence_history" to "Your Registered Absence History",
        "no_leaves" to "No requested leaves found.",
        "coach_comment" to "Coach Comment",
        "profile_title" to "Student Profile",
        "save_profile" to "Save & Sync Profile Changes",
        "field_name" to "Student Name",
        "field_course" to "Course Program",
        "field_batch" to "Assigned Batch",
        "field_mobile" to "Primary Mobile",
        "field_parent_mobile" to "Parent/Guardian Mobile",
        "field_address" to "Physical Address",

        // Coach dashboard translations
        "coach_title" to "Staff Coach Dashboard",
        "leaves_pending" to "LEAVES PENDING",
        "wellness_warnings" to "WELLNESS WARNINGS",
        "critical_alerts" to "Critical Alerts",
        "daily_absent_logs" to "🚨 DAILY ABSENT STUDENT LOGS",
        "no_absents_msg" to "Excellent! No students marked absent today.",
        "pending_leave_reqs" to "Pending Absence / Leave Requests",
        "all_leaves_reviewed" to "All leave applications reviewed! Good job.",
        "coach_remarks_placeholder" to "Add comments or remarks to student...",
        "btn_approve" to "Accept Approve",
        "btn_reject" to "Reject",
        "btn_decision" to "Decision / Remarks",
        "critical_wellness_alerts" to "🔥 Critical Wellness Status Alerts",
        "resolved_warning_msg" to "All athletes wellness values are safe.",
        "student_roster" to "📋 Active Student Roster & Live Stats",
        "admin_title" to "Central Admin Controls",
        "admin_subtitle" to "System Enrollment & Metric Auditing"
    ),
    AppLanguage.TA to mapOf(
        "wellnest_title" to "வெல்நெஸ்ட்",
        "wellnest_subtitle" to "மாணவர்களின் தினசரி நல்வாழ்வு & வருகை பகுப்பாய்வு",
        "switch_role_title" to "போர்டல் பங்கை மாற்றவும்",
        "switch_role_desc" to "நீங்கள் சோதிக்க விரும்பும் டாஷ்போர்டு/பங்கினைத் தேர்ந்தெடுக்கவும்:",
        "switch_lang_title" to "மொழியைத் தேர்ந்தெடுக்கவும்",
        "switch_lang_desc" to "கணினி முழுவதும் காண்பிக்க வேண்டிய மொழியைத் தேர்ந்தெடுக்கவும்:",
        "student_portal" to "🎓 மாணவர் போர்டல்",
        "coach_portal" to "⚽ பயிற்சியாளர் போர்டல்",
        "admin_portal" to "மத்திய நிர்வாக போர்டல்",
        "cancel" to "ரத்து செய்",
        "logout" to "வெளியேறு",
        "exit" to "வெளியேறு",
        "wellness_title" to "தினசரி நல்வாழ்வு கண்காணிப்பு",
        "wellness_subtitle" to "ஆரோக்கியமான அறிக்கையை உருவாக்க தூக்க நேரம், உணவு, ஆற்றல் நிலை ஆகியவற்றை பதிவு செய்யவும்.",
        "sleep_hours_count" to "🛌 தூக்க நேர கணக்கு",
        "meals_question" to "🍽️ இன்று நீங்கள் உணவருந்தினீர்களா?",
        "water_intake" to "💧 தினசரி நீர் உட்கொள்ளல் (லிட்டர்)",
        "mental_vibe" to "🎭 ஒட்டுமொத்த மனநிலை / மன அதிர்வு",
        "wellness_notes" to "📝 மன நல்வாழ்வு குறிப்புகள்",
        "wellness_goals" to "🔮 நாளைக்கான ஆரோக்கியம் மற்றும் நல்வாழ்வு இலக்குகள்",
        "wellness_placeholder" to "இன்று உங்களுக்கு ஏன் இப்படி தோன்றியது? சவால்களை விவரிக்கவும்.",
        "goals_placeholder" to "நாளைக்கு உங்களுக்கு என்ன இலக்குகள் உள்ளன?",
        "success_saved" to "🎉 தினசரி நல்வாழ்வு தரவு வெற்றிகரமாக சேமிக்கப்பட்டது!",
        "btn_log_wellness" to "நல்வாழ்வைச் சேமிக்கவும்",
        "leave_title" to "விடுப்பு மேலாண்மை",
        "leave_subtitle" to "முறையான விடுப்புகளைக் கோரவும், பயிற்சியாளர் மதிப்பாய்வுக்கான சான்றுகளைப் பதிவேற்றவும்.",
        "leave_applicant" to "📄 விண்ணப்பதாரர்",
        "start_date" to "தொடக்க தேதி (yyyy-MM-dd)",
        "end_date" to "முடிவு தேதி (yyyy-MM-dd)",
        "absence_reason" to "விடுப்பிற்கான காரணம்",
        "reason_placeholder" to "விவரங்கள் அல்லது மருத்துவ விபரம்",
        "proof_attachment" to "சான்று இணைப்பு (.jpg/.pdf)",
        "proof_placeholder" to "உதாரணமாக prescription_receipt.pdf",
        "leave_success_msg" to "மதிப்பாய்வுக்காக உங்கள் பயிற்சியாளருக்கு அனுப்பப்பட்டது. கீழே முடிவைக் கண்காணிக்கவும்.",
        "btn_submit_leave" to "விடுப்பு கோரிக்கையை சமர்ப்பிக்கவும்",
        "absence_history" to "உங்களின் பதிவு செய்யப்பட்ட விடுப்பு வரலாறு",
        "no_leaves" to "கோரப்பட்ட விடுப்பு எதுவும் கிடைக்கவில்லை.",
        "coach_comment" to "பயிற்சியாளரின் கருத்து",
        "profile_title" to "மாணவர் சுயவிவரம்",
        "save_profile" to "மாற்றங்களைச் சேமிக்கவும்",
        "field_name" to "மாணவர் பெயர்",
        "field_course" to "பாடநெறி திட்டம்",
        "field_batch" to "ஒதுக்கப்பட்ட தொகுதி",
        "field_mobile" to "முதன்மை மொபைல்",
        "field_parent_mobile" to "பெற்றோர் மொபைல்",
        "field_address" to "இருப்பிட முகவரி",
        "coach_title" to "பயிற்சியாளர் டாஷ்போர்டு",
        "leaves_pending" to "நிலுவையில் உள்ள விடுப்புகள்",
        "wellness_warnings" to "நல்வாழ்வு எச்சரிக்கைகள்",
        "critical_alerts" to "முக்கிய எச்சரிக்கைகள்",
        "daily_absent_logs" to "🚨 தினசரி வருகை தராத மாணவர்கள் பதிவு",
        "no_absents_msg" to "அருமை! இன்று மாணவர்கள் யாரும் விடுப்பு எடுக்கவில்லை.",
        "pending_leave_reqs" to "நிலுவையில் உள்ள விடுப்பு விண்ணப்பங்கள்",
        "all_leaves_reviewed" to "அனைத்து விடுப்பு விண்ணப்பங்களும் மதிப்பாய்வு செய்யப்பட்டுவிட்டன! நன்று.",
        "coach_remarks_placeholder" to "மாணவருக்கான கருத்துகளைச் சேர்க்கவும்...",
        "btn_approve" to "அனுமதி அளிக்கவும்",
        "btn_reject" to "நிராகரிக்கவும்",
        "btn_decision" to "முடிவு விவரம்",
        "critical_wellness_alerts" to "🔥 முக்கிய நல்வாழ்வு எச்சரிக்கைகள்",
        "resolved_warning_msg" to "அனைத்து விளையாட்டு வீரர்களின் நல்வாழ்வு நிலைகளும் பாதுகாப்பாக உள்ளன.",
        "student_roster" to "📋 செயலில் உள்ள மாணவர் பட்டியல் மற்றும் புள்ளிவிவரங்கள்",
        "admin_title" to "மத்திய நிர்வாகக் கட்டுப்பாடுகள்",
        "admin_subtitle" to "சேர்க்கை மற்றும் தணிக்கை"
    )
)

@Composable
fun getTranslation(key: String, viewModel: AppViewModel): String {
    val lang by viewModel.currentLanguage.collectAsState()
    return translations[lang]?.get(key) ?: translations[AppLanguage.EN]?.get(key) ?: key
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: AppViewModel) {
    val authState by viewModel.authState.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val allAttendance by viewModel.allAttendance.collectAsState()
    val allLeaves by viewModel.allLeaves.collectAsState()
    val allWellness by viewModel.allWellness.collectAsState()
    val allFees by viewModel.allFees.collectAsState()
    val allOrganizations by viewModel.allOrganizations.collectAsState()
    val currentStudent by viewModel.currentStudentProfile.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()

    // Base background with modern dynamic color gradient matching light or dark modes
    val bgColorStart = if (isDark) Color(0xFF130A04) else Color(0xFFFFF6F0)
    val bgColorEnd = if (isDark) Color(0xFF070402) else Color(0xFFFFFDFB)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(bgColorStart, bgColorEnd)
                )
            )
    ) {
        when (val state = authState) {
            is AuthState.Unauthenticated -> {
                RoleSelectionAndLoginScreen(viewModel = viewModel)
            }
            is AuthState.OtpVerificationNeeded -> {
                OtpVerificationScreen(
                    state = state,
                    onVerify = { code -> viewModel.verifyOtpAndLogin(code) },
                    onBack = { viewModel.backToLogin() }
                )
            }
            is AuthState.Authenticated -> {
                when (state.role) {
                    "STUDENT" -> {
                        StudentDashboardLayout(
                            viewModel = viewModel,
                            state = state,
                            studentProfile = currentStudent,
                            allAttendance = allAttendance,
                            allLeaves = allLeaves,
                            allWellness = allWellness
                        )
                    }
                    "COACH" -> {
                        CoachDashboardLayout(
                            viewModel = viewModel,
                            state = state,
                            students = allStudents,
                            allAttendance = allAttendance,
                            allLeaves = allLeaves,
                            allWellness = allWellness
                        )
                    }
                    "ADMIN" -> {
                        AdminDashboardLayout(
                            viewModel = viewModel,
                            state = state,
                            students = allStudents,
                            allAttendance = allAttendance,
                            allLeaves = allLeaves,
                            allWellness = allWellness,
                            allFees = allFees,
                            allOrganizations = allOrganizations
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 1. AUTHENTIC LOGIN & ROLE SELECTION SCREEN
// ==========================================
@Composable
fun RoleSelectionAndLoginScreen(viewModel: AppViewModel) {
    var loginStep by remember { mutableStateOf("LOGIN") } // "LOGIN", "ENTER_OTP", "SET_PASSWORD"
    var loginRole by remember { mutableStateOf("STUDENT") } // "STUDENT", "COACH", "ADMIN"
    var academyNameInput by remember { mutableStateOf("Springfield Academy") }
    var mobileNumber by remember { mutableStateOf("9876543210") } // Preloaded student phone
    var password by remember { mutableStateOf("password123") } // Preloaded default password
    var registerNumber by remember { mutableStateOf("2026CS501") } // Preloaded student register
    var enteredOtpCode by remember { mutableStateOf("") }
    var setPasswordVal by remember { mutableStateOf("") }
    var setRegisterNumberVal by remember { mutableStateOf("") }
    
    var signUpRole by remember { mutableStateOf("STUDENT") } // "STUDENT", "COACH", "ADMIN"
    var signUpAcademy by remember { mutableStateOf("Springfield Academy") }
    var signUpSpecialty by remember { mutableStateOf("") }
    
    var isPasswordVisible by remember { mutableStateOf(false) }
    var authFeedbackText by remember { mutableStateOf("") }
    var isFeedbackSuccess by remember { mutableStateOf(false) }
    var hasError by remember { mutableStateOf(false) }
    val isDark by viewModel.isDarkMode.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .windowInsetsPadding(WindowInsets.statusBars),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { viewModel.toggleDarkMode() }) {
                Icon(
                    imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = if (isDark) Color(0xFFFF7A00) else Color(0xFF2E190A)
                )
            }
            IconButton(onClick = { showLanguageDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Change Language",
                    tint = if (isDark) Color(0xFFFF7A00) else Color(0xFF2E190A)
                )
            }
        }

        if (showLanguageDialog) {
            AlertDialog(
                onDismissRequest = { showLanguageDialog = false },
                title = { Text(getTranslation("switch_lang_title", viewModel), fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(getTranslation("switch_lang_desc", viewModel), fontSize = 13.sp, color = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00))
                        Spacer(modifier = Modifier.height(4.dp))
                        AppLanguage.values().forEach { lang ->
                            Button(
                                onClick = {
                                    viewModel.setLanguage(lang)
                                    showLanguageDialog = false
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (currentLang == lang) Color(0xFFFF7A00) else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                )
                            ) {
                                Text(lang.displayName, color = if (currentLang == lang) Color.White else (if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showLanguageDialog = false }) {
                        Text(getTranslation("cancel", viewModel), color = Color(0xFFFF7A00))
                    }
                },
                containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC),
                modifier = Modifier.border(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D), RoundedCornerShape(28.dp))
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = getTranslation("wellnest_title", viewModel),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) Color.White else Color(0xFF180A22),
                textAlign = TextAlign.Center
            )
            Text(
                text = getTranslation("wellnest_subtitle", viewModel),
                fontSize = 13.sp,
                color = if (isDark) Color(0xFFFAF9FF).copy(0.7f) else Color(0xFF331B47),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Card Container for login/otp contents
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (loginStep) {
                        "LOGIN" -> {
                            Text(
                                text = "LOGIN TO WELLNEST",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Role Selection Segment Row
                            Text(
                                text = "SELECT PORTAL ROLE",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("STUDENT", "COACH", "ADMIN").forEach { role ->
                                    val isSelected = loginRole == role
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary
                                                else (if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0))
                                            )
                                            .clickable { 
                                                loginRole = role 
                                                if (role == "COACH") {
                                                    mobileNumber = "9900990099" // preloaded coach
                                                    password = "password123"
                                                    academyNameInput = "Springfield Academy"
                                                } else if (role == "STUDENT") {
                                                    mobileNumber = "9876543210" // preloaded student
                                                    password = "password123"
                                                } else {
                                                    mobileNumber = "8888888888" // preloaded admin
                                                    password = "password123"
                                                }
                                                authFeedbackText = ""
                                                hasError = false
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (role == "COACH") "COACH/STAFF" else role,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else (if (isDark) Color.LightGray else Color.DarkGray)
                                        )
                                    }
                                }
                            }

                            // Optional Academy input based on chosen role (specifically for coach/staff, as requested)
                            if (loginRole == "COACH") {
                                Text(
                                    text = "ACADEMY NAME",
                                    fontSize = 11.sp,
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = academyNameInput,
                                    onValueChange = {
                                        academyNameInput = it
                                        authFeedbackText = ""
                                        hasError = false
                                    },
                                    placeholder = { Text("e.g. Springfield Academy", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                        focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                        unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                    ),
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(Icons.Default.School, contentDescription = "", tint = MaterialTheme.colorScheme.primary.copy(0.7f))
                                    }
                                )
                            }

                            // Enter details form
                            Text(
                                text = "USERNAME (PHONE NUMBER)",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = mobileNumber,
                                onValueChange = {
                                    mobileNumber = it
                                    authFeedbackText = ""
                                    hasError = false
                                },
                                placeholder = { Text("e.g. 9876543210", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                        focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                        unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                ),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Phone, contentDescription = "", tint = MaterialTheme.colorScheme.primary.copy(0.7f))
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "PASSWORD",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    authFeedbackText = ""
                                    hasError = false
                                },
                                placeholder = { Text("Enter your account password", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                    focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                    unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                ),
                                singleLine = true,
                                leadingIcon = {
                                    Icon(Icons.Default.Lock, contentDescription = "", tint = MaterialTheme.colorScheme.primary.copy(0.7f))
                                },
                                trailingIcon = {
                                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                        Icon(
                                            imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle password visibility",
                                            tint = MaterialTheme.colorScheme.primary.copy(0.7f)
                                        )
                                    }
                                }
                            )

                            if (authFeedbackText.isNotEmpty()) {
                                Text(
                                    text = authFeedbackText,
                                    color = if (isFeedbackSuccess) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Button(
                                    onClick = {
                                        if (mobileNumber.trim().isNotEmpty() && password.trim().isNotEmpty()) {
                                            keyboardController?.hide()
                                            viewModel.loginWithPassword(
                                                phone = mobileNumber.trim(),
                                                pass = password.trim(),
                                                role = loginRole,
                                                academy = if (loginRole == "COACH") academyNameInput.trim() else ""
                                            ) { success, msg ->
                                                authFeedbackText = msg
                                                isFeedbackSuccess = success
                                                hasError = !success
                                            }
                                        } else {
                                            authFeedbackText = "Please enter both phone number and password."
                                            isFeedbackSuccess = false
                                            hasError = true
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Sign In Securely", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.White)
                                }

                                OutlinedButton(
                                    onClick = {
                                        if (mobileNumber.trim().isNotEmpty()) {
                                            authFeedbackText = ""
                                            loginStep = "ENTER_OTP" // ASK OTP FIRST BEFORE CREATION
                                        } else {
                                            authFeedbackText = "Please enter your mobile number to send verification OTP."
                                            isFeedbackSuccess = false
                                            hasError = true
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Create One-Time Account", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                            }
                        }
                        "ENTER_OTP" -> {
                            Text(
                                text = "VERIFY MOBILE NUMBER",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "We've simulated sending a 6-digit verification code to $mobileNumber.",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFFFAF9FF).copy(0.7f) else Color(0xFF331B47),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            OutlinedTextField(
                                value = enteredOtpCode,
                                onValueChange = { enteredOtpCode = it },
                                placeholder = { Text("Enter OTP e.g., 123456", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                singleLine = true,
                                textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 16.sp, fontWeight = FontWeight.Bold),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                    focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                    unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                )
                            )

                            if (authFeedbackText.isNotEmpty()) {
                                Text(
                                    text = authFeedbackText,
                                    color = if (isFeedbackSuccess) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        loginStep = "LOGIN"
                                        authFeedbackText = ""
                                    },
                                    modifier = Modifier.weight(1f).height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.2.dp, Color.Gray),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                                ) {
                                    Text("Back", fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (enteredOtpCode.trim().length >= 4) {
                                            isFeedbackSuccess = true
                                            authFeedbackText = "OTP verified successfully!"
                                            loginStep = "SET_PASSWORD" // Next Ask to Set Password!
                                        } else {
                                            isFeedbackSuccess = false
                                            authFeedbackText = "Please enter simulated OTP of at least 4 digits (e.g. 123456)."
                                        }
                                    },
                                    modifier = Modifier.weight(1.2f).height(48.dp),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                                ) {
                                    Text("Verify Code", fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                        "SET_PASSWORD" -> {
                            Text(
                                text = "COMPLETE REGISTRATION",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = "Mobile verified. Choose password, select your role and academy details.",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // 1. CHOOSE ROLE
                            Text(
                                text = "SELECT YOUR PORTAL ROLE",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("STUDENT", "COACH", "ADMIN").forEach { role ->
                                    Button(
                                        onClick = { signUpRole = role },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (signUpRole == role) MaterialTheme.colorScheme.primary else (if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0))
                                        ),
                                        contentPadding = PaddingValues(4.dp)
                                    ) {
                                        Text(
                                            text = role,
                                            fontSize = 11.sp,
                                            color = if (signUpRole == role) Color.White else (if (isDark) Color.LightGray else Color.DarkGray),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 2. CHOOSE PASSWORD
                            Text(
                                text = "CHOOSE PASSWORD",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = setPasswordVal,
                                onValueChange = { setPasswordVal = it },
                                placeholder = { Text("Choose password", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                visualTransformation = PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                    focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                    unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // 3. CHOOSE ACADEMY
                            Text(
                                text = "ACADEMY NAME",
                                fontSize = 11.sp,
                                color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = signUpAcademy,
                                onValueChange = { signUpAcademy = it },
                                placeholder = { Text("e.g. Springfield Academy", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                    focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                    unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                )
                            )

                            // Specific outputs based on roles
                            if (signUpRole == "STUDENT") {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "STUDENT REGISTER NUMBER (OPTIONAL)",
                                    fontSize = 11.sp,
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = setRegisterNumberVal,
                                    onValueChange = { setRegisterNumberVal = it },
                                    placeholder = { Text("e.g. 2026CS504", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                        focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                        unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                    )
                                )
                            } else if (signUpRole == "COACH") {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "COACHING SPECIALTY",
                                    fontSize = 11.sp,
                                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Start)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                OutlinedTextField(
                                    value = signUpSpecialty,
                                    onValueChange = { signUpSpecialty = it },
                                    placeholder = { Text("e.g. Soccer Chief Coach", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                                        unfocusedBorderColor = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1),
                                        focusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC),
                                        unfocusedContainerColor = if (isDark) Color(0xFF0E1428) else Color(0xFFF3F7FC)
                                    )
                                )
                            }

                            if (authFeedbackText.isNotEmpty()) {
                                Text(
                                    text = authFeedbackText,
                                    color = if (isFeedbackSuccess) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    if (setPasswordVal.trim().isNotEmpty()) {
                                        if (signUpRole == "STUDENT") {
                                            val finalReg = if (setRegisterNumberVal.trim().isEmpty()) {
                                                "2026CS" + (500 + (10..99).random())
                                            } else {
                                                setRegisterNumberVal.trim()
                                            }
                                            viewModel.createOneTimeAccount(
                                                phone = mobileNumber.trim(),
                                                pass = setPasswordVal.trim(),
                                                role = "STUDENT",
                                                regNo = finalReg,
                                                academyName = signUpAcademy.trim()
                                            ) { success, msg ->
                                                authFeedbackText = msg
                                                isFeedbackSuccess = success
                                                if (success) {
                                                    password = setPasswordVal.trim()
                                                    loginStep = "LOGIN"
                                                }
                                            }
                                        } else if (signUpRole == "COACH") {
                                            viewModel.addCoachDetails(
                                                name = "Coach ($mobileNumber)",
                                                username = mobileNumber.trim(),
                                                pass = setPasswordVal.trim(),
                                                specialty = signUpSpecialty.ifBlank { "Fitness Trainer" },
                                                academy = signUpAcademy.trim(),
                                                hasAccess = true
                                            )
                                            isFeedbackSuccess = true
                                            authFeedbackText = "Coach Account completed successfully! Please login."
                                            password = setPasswordVal.trim()
                                            loginStep = "LOGIN"
                                        } else { // ADMIN
                                            viewModel.createOneTimeAccount(
                                                phone = mobileNumber.trim(),
                                                pass = setPasswordVal.trim(),
                                                role = "ADMIN",
                                                academyName = signUpAcademy.trim()
                                            ) { success, msg ->
                                                authFeedbackText = msg
                                                isFeedbackSuccess = success
                                                if (success) {
                                                    password = setPasswordVal.trim()
                                                    loginStep = "LOGIN"
                                                }
                                            }
                                        }
                                    } else {
                                        authFeedbackText = "Password cannot be empty."
                                        isFeedbackSuccess = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Complete Account & Sign In", fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Information Callout for quick credentials
        Card(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(24.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1E293B).copy(alpha = 0.5f) else Color(0xFFF1F5F9))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "💡 Quick Access Demo Accounts:",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Student: Phone 9876543210 & Pass password123\n" +
                           "• Coach: Phone 9900990099 & Pass password123\n" +
                           "• Admin: Phone 8888888888 & Pass password123\n" +
                           "• Or create custom account directly using the button above!",
                    fontSize = 11.sp,
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569),
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ==========================================
// 2. Firebase OTP Secure Screen
// ==========================================
@Composable
fun OtpVerificationScreen(
    state: AuthState.OtpVerificationNeeded,
    onVerify: (String) -> Boolean,
    onBack: () -> Unit
) {
    var otpCode by remember { mutableStateOf("") }
    var countdownSeconds by remember { mutableStateOf(59) }
    var codeError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (countdownSeconds > 0) {
            kotlinx.coroutines.delay(1000)
            countdownSeconds--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
        }

        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = "Verify OTP",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "We sent a Firebase verification code to ${state.mobile}",
            fontSize = 13.sp,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(30.dp))

        // PIN code Entry Box
        OutlinedTextField(
            value = otpCode,
            onValueChange = {
                if (it.length <= 6) {
                    otpCode = it
                    codeError = false
                }
            },
            placeholder = { Text("Code like 123456", color = Color(0xFF475569)) },
            modifier = Modifier.fillMaxWidth(0.85f),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color(0xFF10B981),
                unfocusedBorderColor = Color(0xFF334155),
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B)
            ),
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
        )

        if (codeError) {
            Text(
                text = "Invalid verification code limit. Use any 6 characters.",
                color = Color(0xFFEF4444),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (countdownSeconds > 0) "Resend code in ${countdownSeconds}s" else "Didn't receive code? Resend SMS",
            color = if (countdownSeconds > 0) Color(0xFF64748B) else Color(0xFF3B82F6),
            fontSize = 12.sp,
            modifier = Modifier.clickable(enabled = countdownSeconds == 0) {
                countdownSeconds = 59
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                val successful = onVerify(otpCode)
                if (!successful) {
                    codeError = true
                }
            },
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Confirm Verification", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

// ==========================================================
// 3. STUDENT PORTAL LAYOUT
// ==========================================
@Composable
fun StudentDashboardLayout(
    viewModel: AppViewModel,
    state: AuthState.Authenticated,
    studentProfile: StudentProfile?,
    allAttendance: List<AttendanceRecord>,
    allLeaves: List<LeaveApplication>,
    allWellness: List<WellnessEntry>
) {
    var activeTab by remember { mutableStateOf("DASH") } // DASH, ATT, LEAVE, WELL, PROF
    var showEditProfile by remember { mutableStateOf(false) }
    val isDark by viewModel.isDarkMode.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }
    val topBarBg = if (isDark) Color(0xFF16112C) else Color(0xFFFCF5F7)
    val textPrimary = if (isDark) Color.White else Color(0xFF180A22)
    val textSecondary = if (isDark) Color(0xFFE2E0FF).copy(0.7f) else Color(0xFF331B47)

    // Synchronize selected profile manually in case ID changes
    LaunchedEffect(state.registerNumber) {
        viewModel.setStudentDirectly(state.registerNumber)
    }

    val studentLogs = allAttendance.filter { it.registerNumber == state.registerNumber }
    val studentWellness = allWellness.filter { it.registerNumber == state.registerNumber }
    val studentLeaves = allLeaves.filter { it.studentRegister == state.registerNumber }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(topBarBg)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF43F5E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (studentProfile?.name?.take(2) ?: "ST").uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = studentProfile?.name ?: state.name,
                                color = textPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ID: ${state.registerNumber}",
                                color = textSecondary,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "🏫 ${studentProfile?.academyName ?: state.academyName}",
                                color = if (isDark) Color(0xFFFF9E7D) else Color(0xFFFF7A00),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = if (isDark) Color(0xFFFF7A00) else Color(0xFF2E190A)
                            )
                        }
                        IconButton(onClick = { showLanguageDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Change Language",
                                tint = if (isDark) Color(0xFFFF7A00) else Color(0xFF2E190A)
                            )
                        }

                        var showRoleMenu by remember { mutableStateOf(false) }
                        IconButton(onClick = { showRoleMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.ManageAccounts,
                                contentDescription = "Switch Portal Role",
                                tint = if (isDark) Color(0xFFFF7A00) else Color(0xFF2E190A)
                            )
                        }
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Log out",
                                tint = Color(0xFFEF4444)
                            )
                        }

                        if (showLanguageDialog) {
                            AlertDialog(
                                onDismissRequest = { showLanguageDialog = false },
                                title = { Text(getTranslation("switch_lang_title", viewModel), fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)) },
                                text = {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(getTranslation("switch_lang_desc", viewModel), fontSize = 13.sp, color = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        AppLanguage.values().forEach { lang ->
                                            Button(
                                                onClick = {
                                                    viewModel.setLanguage(lang)
                                                    showLanguageDialog = false
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (currentLang == lang) Color(0xFFFF7A00) else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                                )
                                            ) {
                                                Text(lang.displayName, color = if (currentLang == lang) Color.White else (if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                },
                                confirmButton = {},
                                dismissButton = {
                                    TextButton(onClick = { showLanguageDialog = false }) {
                                        Text(getTranslation("cancel", viewModel), color = Color(0xFFFF7A00))
                                    }
                                },
                                containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC),
                                modifier = Modifier.border(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D), RoundedCornerShape(28.dp))
                            )
                        }

                        if (showRoleMenu) {
                            AlertDialog(
                                onDismissRequest = { showRoleMenu = false },
                                title = { Text(getTranslation("switch_role_title", viewModel), fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)) },
                                text = {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(getTranslation("switch_role_desc", viewModel), fontSize = 13.sp, color = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        listOf(
                                            "STUDENT" to getTranslation("student_portal", viewModel),
                                            "COACH" to getTranslation("coach_portal", viewModel),
                                            "ADMIN" to getTranslation("admin_portal", viewModel)
                                        ).forEach { (roleCode, label) ->
                                            Button(
                                                onClick = {
                                                    viewModel.forceSwitchRole(roleCode)
                                                    showRoleMenu = false
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (state.role == roleCode) Color(0xFFFF7A00) else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                                )
                                            ) {
                                                Text(label, color = if (state.role == roleCode) Color.White else (if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                },
                                confirmButton = {},
                                dismissButton = {
                                    TextButton(onClick = { showRoleMenu = false }) {
                                        Text(getTranslation("cancel", viewModel), color = Color(0xFFFF7A00))
                                    }
                                },
                                containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC),
                                modifier = Modifier.border(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D), RoundedCornerShape(28.dp))
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = topBarBg,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                val menu = listOf(
                    Triple("DASH", "Home", Icons.Default.Home),
                    Triple("ATT", "Reports", Icons.Default.EventAvailable),
                    Triple("WELL", "Wellness", Icons.Default.Favorite),
                    Triple("LEAVE", "Leaves", Icons.Default.Send),
                    Triple("BILL", "Billing", Icons.Default.Payments),
                    Triple("PROF", "Profile", Icons.Default.Person)
                )

                menu.forEach { (code, title, icon) ->
                    val isSel = activeTab == code
                    NavigationBarItem(
                        selected = isSel,
                        onClick = { activeTab = code },
                        label = { Text(title, fontSize = 9.sp) },
                        icon = { Icon(icon, contentDescription = title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                "DASH" -> StudentHomeTab(
                    viewModel = viewModel,
                    state = state,
                    regNo = state.registerNumber,
                    studentProfile = studentProfile,
                    attendance = studentLogs,
                    leaves = studentLeaves,
                    wellness = studentWellness
                )
                "ATT" -> StudentAttendanceTab(
                    studentLogs = studentLogs,
                    regNo = state.registerNumber,
                    viewModel = viewModel
                )
                "WELL" -> StudentWellnessTab(
                    registerNumber = state.registerNumber,
                    wellnessList = studentWellness,
                    isDark = isDark,
                    viewModel = viewModel,
                    onSubmit = { sleep, b, l, d, water, energy, mood, n, i ->
                        viewModel.submitWellness(state.registerNumber, sleep, b, l, d, water, energy, mood, n, i)
                    }
                )
                "LEAVE" -> StudentLeavesTab(
                    registerNumber = state.registerNumber,
                    studentName = studentProfile?.name ?: state.name,
                    leavesList = studentLeaves,
                    isDark = isDark,
                    viewModel = viewModel,
                    onApply = { start, end, reason, proof ->
                        viewModel.applyForLeave(state.registerNumber, studentProfile?.name ?: state.name, start, end, reason, proof)
                    }
                )
                 "BILL" -> StudentBillingTab(
                    viewModel = viewModel,
                    state = state,
                    isDark = isDark
                )
                "PROF" -> StudentProfileTab(
                    studentProfile = studentProfile,
                    isDark = isDark,
                    viewModel = viewModel,
                    onSave = { updated ->
                        viewModel.saveStudentProfile(updated)
                    }
                )
            }
        }
    }
}

// ------------------------------------------
// Student Home Subtab (Visual Overview)
// ------------------------------------------
@Composable
fun StudentHomeTab(
    viewModel: AppViewModel,
    state: AuthState.Authenticated,
    regNo: String,
    studentProfile: StudentProfile?,
    attendance: List<AttendanceRecord>,
    leaves: List<LeaveApplication>,
    wellness: List<WellnessEntry>
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Metrics calculations
    val totalCount = attendance.size
    val presentCount = attendance.count { it.status == "Present" }
    val lateCount = attendance.count { it.status == "Late" }
    val attendancePct = if (totalCount > 0) {
        ((presentCount + (lateCount * 0.7f)) / totalCount * 100).toInt()
    } else {
        100
    }

    // Checking of today's logs
    val morningLogged = attendance.any { it.date == todayDateStr && it.shift == "Morning" }
    val eveningLogged = attendance.any { it.date == todayDateStr && it.shift == "Evening" }

    val isDark by viewModel.isDarkMode.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header message
            Column {
                Text(
                    text = "Welcome back,",
                    fontSize = 14.sp,
                    color = if (isDark) Color(0xFFE2E0FF).copy(0.7f) else Color(0xFF331B47).copy(0.7f)
                )
                Text(
                    text = studentProfile?.name ?: state.name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDark) Color.White else Color(0xFF180A22)
                )
                Text(
                    text = "Track your attendance status and mental energy level today.",
                    fontSize = 11.sp,
                    color = Color(0xFFFF8A65)
                )
            }
        }

        // Attendance Stats Gauge Panel
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)),
                border = BorderStroke(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(90.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Drawing dynamic meter using Compose Canvas
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = Color(0xFFFFD8C2),
                                startAngle = -220f,
                                sweepAngle = 260f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = if (attendancePct >= 75) Color(0xFF10B981) else Color(0xFFFF7A00),
                                startAngle = -220f,
                                sweepAngle = (attendancePct / 100f) * 260f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$attendancePct%",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "ATTENDANCE",
                                fontSize = 7.sp,
                                color = if (isDark) Color(0xFFE2E0FF).copy(0.7f) else Color(0xFF331B47).copy(0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = "COMPLIANCE STATUS",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        val criteriaMet = attendancePct >= 75
                        Text(
                            text = if (criteriaMet) "Good Standing ✅" else "Below Criteria ⚠️",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (criteriaMet) Color(0xFF10B981) else Color(0xFFEF4444)
                        )
                        Text(
                            text = if (criteriaMet) "Maintain 75%+ to qualify for year-end university schedules." else "Action needed: submit leave proof files inside portal immediately.",
                            fontSize = 10.sp,
                            color = if (isDark) Color(0xFFE2E0FF).copy(0.7f) else Color(0xFF331B47).copy(0.7f),
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }

        // Daily Check-ins (Morning / Evening Attendance Status)
        item {
            val morningRecord = attendance.find { it.date == todayDateStr && it.shift == "Morning" }
            val eveningRecord = attendance.find { it.date == todayDateStr && it.shift == "Evening" }
            
            val morningStatus = morningRecord?.status ?: "Not Marked Yet"
            val eveningStatus = eveningRecord?.status ?: "Not Marked Yet"

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)),
                border = BorderStroke(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📅 TODAY'S ATTENDANCE STATUS",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Morning Check-in Card (Read-only status)
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when (morningStatus) {
                                        "Present" -> if (isDark) Color(0xFF042F1A) else Color(0xFFDCFCE7)
                                        "Absent" -> if (isDark) Color(0xFF450A0A) else Color(0xFFFEE2E2)
                                        "Late" -> if (isDark) Color(0xFF451A03) else Color(0xFFFEF3C7)
                                        else -> if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)
                                    }
                                )
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (morningStatus) {
                                    "Present" -> Icons.Default.CheckCircle
                                    "Absent" -> Icons.Default.Cancel
                                    "Late" -> Icons.Default.Schedule
                                    else -> Icons.Default.WbSunny
                                },
                                contentDescription = "",
                                tint = when (morningStatus) {
                                    "Present" -> Color(0xFF10B981)
                                    "Absent" -> Color(0xFFEF4444)
                                    "Late" -> Color(0xFFF59E0B)
                                    else -> Color(0xFF94A3B8)
                                },
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Morning Shift", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = morningStatus,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = when (morningStatus) {
                                    "Present" -> Color(0xFF10B981)
                                    "Absent" -> Color(0xFFEF4444)
                                    "Late" -> Color(0xFFF59E0B)
                                    else -> if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                                }
                            )
                        }

                        // Evening Check-in Card (Read-only status)
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    when (eveningStatus) {
                                        "Present" -> if (isDark) Color(0xFF042F1A) else Color(0xFFDCFCE7)
                                        "Absent" -> if (isDark) Color(0xFF450A0A) else Color(0xFFFEE2E2)
                                        "Late" -> if (isDark) Color(0xFF451A03) else Color(0xFFFEF3C7)
                                        else -> if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9)
                                    }
                                )
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (eveningStatus) {
                                    "Present" -> Icons.Default.CheckCircle
                                    "Absent" -> Icons.Default.Cancel
                                    "Late" -> Icons.Default.Schedule
                                    else -> Icons.Default.NightsStay
                                },
                                contentDescription = "",
                                tint = when (eveningStatus) {
                                    "Present" -> Color(0xFF10B981)
                                    "Absent" -> Color(0xFFEF4444)
                                    "Late" -> Color(0xFFF59E0B)
                                    else -> Color(0xFF94A3B8)
                                },
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Evening Shift", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = eveningStatus,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = when (eveningStatus) {
                                    "Present" -> Color(0xFF10B981)
                                    "Absent" -> Color(0xFFEF4444)
                                    "Late" -> Color(0xFFF59E0B)
                                    else -> if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)
                                }
                            )
                        }
                    }
                }
            }
        }

        // Wellness metrics scoring card
        item {
            val lastWellnessEntry = wellness.firstOrNull()
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)),
                border = BorderStroke(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🍀 WEEKLY WELLNESS PROFILE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE65100).copy(0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Mood: ${lastWellnessEntry?.mood ?: "Not Logged"}",
                                fontSize = 10.sp,
                                color = Color(0xFFE65100),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (lastWellnessEntry == null) {
                        Text(
                            text = "No wellness entries submitted recently. Press the 'Wellness' tab in bottom bar to record meals, sleep hours, water intake, and build your wellness score.",
                            fontSize = 11.sp,
                            color = if (isDark) Color(0xFFFAF9FF).copy(0.7f) else Color(0xFF1E293B).copy(0.7f),
                            lineHeight = 16.sp
                        )
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.LocalHotel, contentDescription = "", tint = MaterialTheme.colorScheme.primary)
                                Text("Sleep", fontSize = 11.sp, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                                Text("${lastWellnessEntry.sleepHours} hrs", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.LocalDrink, contentDescription = "", tint = Color(0xFF0284C7))
                                Text("Water", fontSize = 11.sp, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                                Text("${String.format(Locale.getDefault(), "%.2f", lastWellnessEntry.waterIntakeCups * 0.25f)} L", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        // Progress / Checklist summary
                        Text(
                            text = "Daily Meals Taken:",
                            fontSize = 11.sp,
                            color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                            fontWeight = FontWeight.SemiBold
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            listOf(
                                "Breakfast" to lastWellnessEntry.hadBreakfast,
                                "Lunch" to lastWellnessEntry.hadLunch,
                                "Dinner" to lastWellnessEntry.hadDinner
                            ).forEach { (mealName, didEat) ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (didEat) Color(0xFF10B981).copy(0.15f) else Color(0xFFEF4444).copy(0.15f))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "$mealName: ${if (didEat) "Yes" else "No"}",
                                        fontSize = 10.sp,
                                        color = if (didEat) Color(0xFF10B981) else Color(0xFFEF4444)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Leaves summary
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)),
                border = BorderStroke(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📬 LEAVE APPLICATION STATUSES",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (leaves.isEmpty()) {
                        Text(
                            text = "All leave registers are clear. You can request formal absences inside the Leaves screen.",
                            fontSize = 11.sp,
                            color = if (isDark) Color(0xFFFAF9FF).copy(0.7f) else Color(0xFF1E293B).copy(0.7f)
                        )
                    } else {
                        leaves.take(2).forEach { leave ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = "Reason: ${leave.reason}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                    Text(text = "${leave.startDate} to ${leave.endDate}", fontSize = 10.sp, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                                }
                                val (bg, textCol) = when(leave.status) {
                                    "Pending" -> Color(0xFFF59E0B).copy(0.15f) to Color(0xFFF59E0B)
                                    "Approved" -> Color(0xFF10B981).copy(0.15f) to Color(0xFF10B981)
                                    else -> Color(0xFFEF4444).copy(0.15f) to Color(0xFFEF4444)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(bg)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(leave.status, color = textCol, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------
// Student Attendance Reports Subtab
// ------------------------------------------
@Composable
fun StudentAttendanceTab(
    studentLogs: List<AttendanceRecord>,
    regNo: String,
    viewModel: AppViewModel
) {
    val isDark by viewModel.isDarkMode.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Attendance Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A))
        Text("Detailed record of daily check-ins for register number: $regNo", fontSize = 11.sp, color = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00))
        Spacer(modifier = Modifier.height(16.dp))

        if (studentLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Event, contentDescription = "", tint = if (isDark) Color(0xFFFFB088).copy(0.4f) else Color(0xFFFF9E7D).copy(0.4f), modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No logs recorded yet.", color = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00), fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(studentLogs) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)),
                        border = BorderStroke(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = item.date, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold)
                                Text(text = "Shift: ${item.shift}", fontSize = 11.sp, color = Color(0xFFE65100))
                            }
                            val (pillBg, pillText) = when (item.status.uppercase(Locale.US)) {
                                "PRESENT" -> Color(0xFF10B981).copy(0.15f) to Color(0xFF10B981)
                                "LATE" -> Color(0xFFF59E0B).copy(0.15f) to Color(0xFFF59E0B)
                                else -> Color(0xFFEF4444).copy(0.15f) to Color(0xFFEF4444)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(pillBg)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(text = item.status, color = pillText, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ------------------------------------------
// Student Wellness Entry Tab
// ------------------------------------------
@Composable
fun StudentWellnessTab(
    registerNumber: String,
    wellnessList: List<WellnessEntry>,
    isDark: Boolean = false,
    viewModel: AppViewModel,
    onSubmit: (Float, Boolean, Boolean, Boolean, Int, Int, String, String, String) -> Unit
) {
    var sleepHours by remember { mutableStateOf(7.5f) }
    var brekkie by remember { mutableStateOf(true) }
    var lunch by remember { mutableStateOf(true) }
    var dinner by remember { mutableStateOf(true) }
    var waterCups by remember { mutableStateOf(6) }
    var energy by remember { mutableStateOf(7) }
    var selectedMood by remember { mutableStateOf("Calm") }
    var notes by remember { mutableStateOf("") }
    var improvements by remember { mutableStateOf("") }

    var showSuccessToast by remember { mutableStateOf(false) }

    val moodChips = listOf(
        "Happy" to "😊",
        "Tired" to "😴",
        "Stressed" to "😟",
        "Calm" to "😌",
        "Focused" to "🎯"
    )

    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val cardBorder = if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(getTranslation("wellness_title", viewModel), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text(getTranslation("wellness_subtitle", viewModel), fontSize = 11.sp, color = textSecondary)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.5.dp, cardBorder),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Sleep Hours Slider
                Text(
                    text = "${getTranslation("sleep_hours_count", viewModel)}: ${String.format(Locale.US, "%.1f", sleepHours)} hrs",
                    color = textPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Slider(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    valueRange = 1f..12f,
                    steps = 22,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFFF6F00),
                        activeTrackColor = Color(0xFFFF6F00),
                        inactiveTrackColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Meals Checklist
                Text(getTranslation("meals_question", viewModel), color = textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val meals = listOf("Breakfast" to brekkie, "Lunch" to lunch, "Dinner" to dinner)
                    meals.forEach { (name, checked) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable {
                                when (name) {
                                    "Breakfast" -> brekkie = !brekkie
                                    "Lunch" -> lunch = !lunch
                                    "Dinner" -> dinner = !dinner
                                }
                            }
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    when (name) {
                                        "Breakfast" -> brekkie = it
                                        "Lunch" -> lunch = it
                                        "Dinner" -> dinner = it
                                    }
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFFF6F00))
                            )
                            Text(name, color = textPrimary, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Water intake counter
                Text("${getTranslation("water_intake", viewModel)}: ${String.format(Locale.getDefault(), "%.2f", waterCups * 0.25f)} L", color = textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { if (waterCups > 0) waterCups-- },
                        modifier = Modifier
                            .size(36.dp)
                            .background(if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6), CircleShape)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Minus", tint = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00))
                    }
                    Text("${String.format(Locale.getDefault(), "%.2f", waterCups * 0.25f)} L", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    IconButton(
                        onClick = { waterCups++ },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFFF6F00), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Plus", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mood selector
                Text(getTranslation("mental_vibe", viewModel), color = textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                SingleRowChipSelection(
                    items = moodChips,
                    selectedItem = selectedMood,
                    isDark = isDark,
                    onSelect = { selectedMood = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notes Field
                Text(getTranslation("wellness_notes", viewModel), color = textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text(getTranslation("wellness_placeholder", viewModel), color = if (isDark) Color(0xFFFFB088).copy(0.6f) else Color(0xFF8C3E00).copy(0.6f), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                        focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                        focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Improvements field
                Text(getTranslation("wellness_goals", viewModel), color = textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = improvements,
                    onValueChange = { improvements = it },
                    placeholder = { Text(getTranslation("goals_placeholder", viewModel), color = if (isDark) Color(0xFFFFB088).copy(0.6f) else Color(0xFF8C3E00).copy(0.6f), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                        focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                        focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (showSuccessToast) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF7A00).copy(0.15f))
                            .border(1.dp, Color(0xFFFF7A00), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(getTranslation("success_saved", viewModel), color = Color(0xFFFF7A00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        onSubmit(sleepHours, brekkie, lunch, dinner, waterCups, energy, selectedMood, notes, improvements)
                        showSuccessToast = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00))
                ) {
                    Text(getTranslation("btn_log_wellness", viewModel), fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun SingleRowChipSelection(
    items: List<Pair<String, String>>,
    selectedItem: String,
    isDark: Boolean = false,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { (code, emoji) ->
            val isSel = selectedItem == code
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSel) Color(0xFFFF7A00) else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6)))
                    .clickable { onSelect(code) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = code,
                        color = if (isSel) Color.White else (if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ------------------------------------------
// Student Leaves Apply Tab
// ------------------------------------------
@Composable
fun StudentLeavesTab(
    registerNumber: String,
    studentName: String,
    leavesList: List<LeaveApplication>,
    isDark: Boolean = false,
    viewModel: AppViewModel,
    onApply: (String, String, String, String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var proofName by remember { mutableStateOf("") }
    var wasSubmitted by remember { mutableStateOf(false) }

    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val cardBorder = if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(getTranslation("leaves_title", viewModel), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text(getTranslation("leaves_subtitle", viewModel), fontSize = 11.sp, color = textSecondary)
        Spacer(modifier = Modifier.height(16.dp))

        // Create form
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.5.dp, cardBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("${getTranslation("applicant_label", viewModel)}: $studentName", fontSize = 12.sp, color = Color(0xFFFF7A00), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(getTranslation("start_date_label", viewModel), color = textPrimary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { startDate = it },
                            placeholder = { Text("2026-06-01", color = if (isDark) Color(0xFFFFB088).copy(0.6f) else Color(0xFF8C3E00).copy(0.6f), fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                                focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                                focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                            )
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(getTranslation("end_date_label", viewModel), color = textPrimary, fontSize = 11.sp)
                        OutlinedTextField(
                            value = endDate,
                            onValueChange = { endDate = it },
                            placeholder = { Text("2026-06-03", color = if (isDark) Color(0xFFFFB088).copy(0.6f) else Color(0xFF8C3E00).copy(0.6f), fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                                focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                                focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(getTranslation("absence_reason_label", viewModel), color = textPrimary, fontSize = 11.sp)
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    placeholder = { Text(getTranslation("absence_reason_placeholder", viewModel), color = if (isDark) Color(0xFFFFB088).copy(0.6f) else Color(0xFF8C3E00).copy(0.6f), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                        focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                        focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(getTranslation("verification_proof_label", viewModel), color = textPrimary, fontSize = 11.sp)
                OutlinedTextField(
                    value = proofName,
                    onValueChange = { proofName = it },
                    placeholder = { Text(getTranslation("verification_proof_placeholder", viewModel), color = if (isDark) Color(0xFFFFB088).copy(0.6f) else Color(0xFF8C3E00).copy(0.6f), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AttachFile, contentDescription = "", tint = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                        focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                        focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (wasSubmitted) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFF7A00).copy(0.15f))
                            .border(1.dp, Color(0xFFFF7A00), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(getTranslation("leaves_success", viewModel), color = Color(0xFFFF7A00), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        if (startDate.isNotBlank() && endDate.isNotBlank() && reason.isNotBlank()) {
                            onApply(startDate, endDate, reason, proofName.ifBlank { "attached_receipt.jpg" })
                            wasSubmitted = true
                            reason = ""
                            startDate = ""
                            endDate = ""
                            proofName = ""
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00))
                ) {
                    Text(getTranslation("btn_submit_absence", viewModel), fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // History listing
        Text(getTranslation("history_title", viewModel), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        if (leavesList.isEmpty()) {
            Text(getTranslation("history_empty", viewModel), color = textSecondary, fontSize = 12.sp)
        } else {
            leavesList.forEach { leave ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = BorderStroke(1.5.dp, cardBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${leave.startDate} to ${leave.endDate}", fontSize = 13.sp, color = textPrimary, fontWeight = FontWeight.Bold)
                            val (bg, tCol) = when (leave.status.uppercase(Locale.US)) {
                                "PENDING" -> Color(0xFFFFB088).copy(0.15f) to Color(0xFFFF7A00)
                                "APPROVED" -> Color(0xFF10B981).copy(0.15f) to Color(0xFF10B981)
                                else -> Color(0xFFEF4444).copy(0.15f) to Color(0xFFEF4444)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(leave.status, color = tCol, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Text(text = "${getTranslation("reason_prefix", viewModel)}: ${leave.reason}", fontSize = 12.sp, color = textSecondary, modifier = Modifier.padding(top = 4.dp))
                        Text(text = "${getTranslation("proof_prefix", viewModel)}: ${leave.proofName}", fontSize = 10.sp, color = textSecondary.copy(0.8f))

                        if (leave.remarks.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB))
                                    .border(1.dp, if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6), RoundedCornerShape(6.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Coach Comment:", fontSize = 10.sp, color = Color(0xFFFF7A00), fontWeight = FontWeight.Bold)
                                    Text(leave.remarks, fontSize = 11.sp, color = textPrimary)
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

// ------------------------------------------
// Student Profile Tab (Editable)
// ------------------------------------------
@Composable
fun StudentProfileTab(
    studentProfile: StudentProfile?,
    isDark: Boolean = false,
    viewModel: AppViewModel,
    onSave: (StudentProfile) -> Unit
) {
    if (studentProfile == null) return

    var name by remember { mutableStateOf(studentProfile.name) }
    var address by remember { mutableStateOf(studentProfile.address) }
    var mobile by remember { mutableStateOf(studentProfile.mobileNumber) }
    var parentMobile by remember { mutableStateOf(studentProfile.parentMobile) }
    var batch by remember { mutableStateOf(studentProfile.batch) }
    var course by remember { mutableStateOf(studentProfile.course) }

    var isEditState by remember { mutableStateOf(false) }

    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val cardBorder = if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(getTranslation("profile_title", viewModel), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            IconButton(onClick = { isEditState = !isEditState }) {
                Icon(
                    imageVector = if (isEditState) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = "",
                    tint = Color(0xFFFF7A00)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Large Profile Card
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF7A00)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    name.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(studentProfile.registerNumber, fontSize = 14.sp, color = Color(0xFFFF7A00), fontWeight = FontWeight.Bold)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.5.dp, cardBorder),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Name
                ProfileFieldItem(label = getTranslation("field_name", viewModel), value = name, enabled = isEditState, isDark = isDark, onValueChange = { name = it })
                // Course
                ProfileFieldItem(label = getTranslation("field_course", viewModel), value = course, enabled = isEditState, isDark = isDark, onValueChange = { course = it })
                // Batch
                ProfileFieldItem(label = getTranslation("field_batch", viewModel), value = batch, enabled = isEditState, isDark = isDark, onValueChange = { batch = it })
                // Mobile
                ProfileFieldItem(label = getTranslation("field_mobile", viewModel), value = mobile, enabled = isEditState, isDark = isDark, onValueChange = { mobile = it })
                // Parent Mobile
                ProfileFieldItem(label = getTranslation("field_parent_mobile", viewModel), value = parentMobile, enabled = isEditState, isDark = isDark, onValueChange = { parentMobile = it })
                // Address
                ProfileFieldItem(label = getTranslation("field_address", viewModel), value = address, enabled = isEditState, isDark = isDark, onValueChange = { address = it })

                if (isEditState) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            val updated = studentProfile.copy(
                                name = name,
                                address = address,
                                mobileNumber = mobile,
                                parentMobile = parentMobile,
                                batch = batch,
                                course = course
                            )
                            onSave(updated)
                            isEditState = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00))
                    ) {
                        Text(getTranslation("save_btn", viewModel), fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ProfileFieldItem(
    label: String,
    value: String,
    enabled: Boolean,
    isDark: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    Column {
        Text(label, color = Color(0xFFFF7A00), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        if (enabled) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                    focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                    focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                )
            )
        } else {
            Text(value, color = textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
            Divider(color = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6), modifier = Modifier.padding(top = 6.dp))
        }
    }
}


@Composable
fun StudentBillingTab(
    viewModel: AppViewModel,
    state: AuthState.Authenticated,
    isDark: Boolean
) {
    val allFees by viewModel.allFees.collectAsState()
    val myFees = allFees.filter { it.studentRegister == state.registerNumber }
    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val accentColor = Color(0xFFFF7A00)

    val pendingAmount = myFees.filter { it.status != "Paid" }.sumOf { it.amount }

    var showPaymentDialog by remember { mutableStateOf<StudentFee?>(null) }
    var payMode by remember { mutableStateOf("UPI") }
    var payRef by remember { mutableStateOf("") }
    var payRemarks by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Academy Billing & Invoices", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Text("Track, manage, and complete your coaching and academy fee payments.", fontSize = 11.sp, color = textSecondary)
        Spacer(modifier = Modifier.height(16.dp))

        // Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF2E1202) else Color(0xFFFFF0E6))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("OUTSTANDING BALANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        Text("₹${pendingAmount}", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = textPrimary)
                    }
                    Box(
                        modifier = Modifier
                            .background(if (pendingAmount > 0) Color(0xFFEF4444).copy(0.12f) else Color(0xFF10B981).copy(0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (pendingAmount > 0) "PAYMENTS DUE" else "FULLY PAID",
                            color = if (pendingAmount > 0) Color(0xFFEF4444) else Color(0xFF10B981),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text("Your registered academy: ${state.academyName}", fontSize = 12.sp, color = textSecondary, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (successMessage.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(0.15f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "", tint = Color(0xFF10B981))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(successMessage, color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Text("Registered Fee Invoices", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        Spacer(modifier = Modifier.height(8.dp))

        if (myFees.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Text("No fee invoice records found.", modifier = Modifier.padding(16.dp), color = textSecondary, fontSize = 12.sp)
            }
        } else {
            myFees.forEach { fee ->
                val cardBorder = if (fee.status == "Paid") Color(0xFF10B981) else if (fee.status == "Overdue") Color(0xFFEF4444) else accentColor
                val statusBg = if (fee.status == "Paid") Color(0xFF10B981).copy(0.15f) else if (fee.status == "Overdue") Color(0xFFEF4444).copy(0.15f) else accentColor.copy(0.15f)
                val statusTextCol = if (fee.status == "Paid") Color(0xFF10B981) else if (fee.status == "Overdue") Color(0xFFEF4444) else accentColor

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .border(1.dp, cardBorder.copy(0.3f), RoundedCornerShape(12.dp)),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${fee.month} ${fee.year} Coaching Fees", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("Amount: ₹${fee.amount}", fontSize = 12.sp, color = textSecondary)
                            if (fee.paymentDate.isNotBlank()) {
                                Text("Paid via: ${fee.paymentMode} (${fee.paymentDate})", fontSize = 11.sp, color = textSecondary.copy(0.8f))
                                Text("Ref: ${fee.transactionReference}", fontSize = 10.sp, color = textSecondary.copy(0.6f))
                            } else {
                                Text("Remarks: ${fee.remarks.ifEmpty { "Pending review" }}", fontSize = 11.sp, color = textSecondary)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Box(
                                modifier = Modifier
                                    .background(statusBg, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(fee.status.uppercase(), color = statusTextCol, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            if (fee.status != "Paid") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        showPaymentDialog = fee
                                        payRef = "TXN" + (10000000..99999999).random().toString()
                                        payRemarks = ""
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Pay Now", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showPaymentDialog != null) {
            val f = showPaymentDialog!!
            AlertDialog(
                onDismissRequest = { showPaymentDialog = null },
                title = { Text("Complete Fee Payment", fontWeight = FontWeight.Bold, color = textPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("You are paying ₹${f.amount} for ${f.month} ${f.year} fees.", fontSize = 12.sp, color = textSecondary)

                        Text("Payment Mode", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            listOf("UPI", "Cash", "Bank Transfer").forEach { mode ->
                                Button(
                                    onClick = { payMode = mode },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (payMode == mode) accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                    ),
                                    modifier = Modifier.weight(1f),
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    Text(mode, fontSize = 10.sp, color = if (payMode == mode) Color.White else textPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Text("Transaction Reference Line / UPI ID", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        OutlinedTextField(
                            value = payRef,
                            onValueChange = { payRef = it },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(fontSize = 12.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            )
                        )

                        Text("Optional Note", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                        OutlinedTextField(
                            value = payRemarks,
                            onValueChange = { payRemarks = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Paid online", fontSize = 12.sp) },
                            textStyle = TextStyle(fontSize = 12.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = accentColor,
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val updatedFee = f.copy(
                                status = "Paid",
                                paymentDate = todayStr,
                                paymentMode = payMode,
                                transactionReference = payRef,
                                remarks = payRemarks.ifBlank { "Paid by mobile app" }
                            )
                            viewModel.updateFeeStatus(updatedFee)
                            successMessage = "Successfully completed ₹${f.amount} payment for ${f.month} ${f.year}!"
                            showPaymentDialog = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Text("Confirm Payment", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPaymentDialog = null }) {
                        Text("Cancel", color = accentColor)
                    }
                },
                containerColor = cardBg,
                modifier = Modifier.border(1.5.dp, accentColor, RoundedCornerShape(28.dp))
            )
        }
    }
}


// ==========================================================
// 4. COACH PORTAL LAYOUT
// ==========================================================
data class CustomTournament(val title: String, val date: String, val location: String)
@Composable
fun CoachDashboardLayout(
    viewModel: AppViewModel,
    state: AuthState.Authenticated,
    students: List<StudentProfile>,
    allAttendance: List<AttendanceRecord>,
    allLeaves: List<LeaveApplication>,
    allWellness: List<WellnessEntry>
) {
    var selectedLeaveForRemark by remember { mutableStateOf<LeaveApplication?>(null) }
    var coachRemarks by remember { mutableStateOf("") }
    val currentLang by viewModel.currentLanguage.collectAsState()
    var showLanguageDialog by remember { mutableStateOf(false) }

    val coachAcademy = state.academyName
    val filteredStudents = students.filter { it.academyName == coachAcademy }
    val academyRegs = filteredStudents.map { it.registerNumber }.toSet()

    val filteredAttendance = allAttendance.filter { it.registerNumber in academyRegs }
    val filteredLeaves = allLeaves.filter { it.studentRegister in academyRegs }
    val filteredWellness = allWellness.filter { it.registerNumber in academyRegs }

    // Counts
    val pendingLeavesCount = filteredLeaves.count { it.status == "Pending" }
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val absentCount = filteredAttendance.filter { it.date == todayStr && it.status == "Absent" }.map { it.registerNumber }.distinct().size

    // Wellness alerts count (Students with sleep hours < 5.0)
    val criticalAlerts = filteredWellness.filter { it.sleepHours <= 5f }

    val isDark by viewModel.isDarkMode.collectAsState()
    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val cardBorder = if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)
    val topBarBg = if (isDark) Color(0xFF1E1107) else Color(0xFFFFF0E6)
    val accentColor = Color(0xFFFF7A00)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(topBarBg)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = state.name, color = textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Role: Staff Coach Dashboard", color = accentColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = accentColor
                            )
                        }
                        IconButton(onClick = { showLanguageDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Change Language",
                                tint = accentColor
                            )
                        }

                        var showRoleMenu by remember { mutableStateOf(false) }
                        IconButton(onClick = { showRoleMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.ManageAccounts,
                                contentDescription = "Switch Portal Role",
                                tint = accentColor
                            )
                        }
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit", tint = Color(0xFFEF4444))
                        }

                        if (showLanguageDialog) {
                            AlertDialog(
                                onDismissRequest = { showLanguageDialog = false },
                                title = { Text(getTranslation("switch_lang_title", viewModel), fontWeight = FontWeight.Bold, color = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)) },
                                text = {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(getTranslation("switch_lang_desc", viewModel), fontSize = 13.sp, color = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00))
                                        Spacer(modifier = Modifier.height(4.dp))
                                        AppLanguage.values().forEach { lang ->
                                            Button(
                                                onClick = {
                                                    viewModel.setLanguage(lang)
                                                    showLanguageDialog = false
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (currentLang == lang) Color(0xFFFF7A00) else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                                )
                                            ) {
                                                Text(lang.displayName, color = if (currentLang == lang) Color.White else (if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                },
                                confirmButton = {},
                                dismissButton = {
                                    TextButton(onClick = { showLanguageDialog = false }) {
                                        Text(getTranslation("cancel", viewModel), color = Color(0xFFFF7A00))
                                    }
                                },
                                containerColor = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC),
                                modifier = Modifier.border(1.5.dp, if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D), RoundedCornerShape(28.dp))
                            )
                        }

                        if (showRoleMenu) {
                            AlertDialog(
                                onDismissRequest = { showRoleMenu = false },
                                title = { Text(getTranslation("switch_role_title", viewModel), fontWeight = FontWeight.Bold, color = textPrimary) },
                                text = {
                                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                        Text(getTranslation("switch_role_desc", viewModel), fontSize = 13.sp, color = textSecondary)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        listOf(
                                            "STUDENT" to getTranslation("student_portal", viewModel),
                                            "COACH" to getTranslation("coach_portal", viewModel),
                                            "ADMIN" to getTranslation("admin_portal", viewModel)
                                        ).forEach { (roleCode, label) ->
                                            Button(
                                                onClick = {
                                                    viewModel.forceSwitchRole(roleCode)
                                                    showRoleMenu = false
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = if (state.role == roleCode) accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                                )
                                            ) {
                                                Text(label, color = if (state.role == roleCode) Color.White else textPrimary, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                },
                                confirmButton = {},
                                dismissButton = {
                                    TextButton(onClick = { showRoleMenu = false }) {
                                        Text(getTranslation("cancel", viewModel), color = accentColor)
                                    }
                                },
                                containerColor = cardBg,
                                modifier = Modifier.border(1.5.dp, cardBorder, RoundedCornerShape(28.dp))
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Stats row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = BorderStroke(1.5.dp, cardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("LEAVES PENDING", fontSize = 10.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                        Text("$pendingLeavesCount Requests", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = BorderStroke(1.5.dp, cardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("WELLNESS WARNINGS", fontSize = 10.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                        Text("${criticalAlerts.size} Critical Alerts", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                    }
                }
            }

            // Daily Attendance Management Panel
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.5.dp, cardBorder),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📋 DAILY ATTENDANCE MANAGEMENT PANEL",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = "Track, review, and record daily shift attendance for $coachAcademy students.",
                        fontSize = 11.sp,
                        color = textSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    var selectedAttendanceDate by remember { mutableStateOf(todayStr) }
                    var selectedShiftState by remember { mutableStateOf("Morning") }

                    // Date and Shift Selection Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = selectedAttendanceDate,
                            onValueChange = { selectedAttendanceDate = it },
                            label = { Text("Attendance Date", fontSize = 10.sp) },
                            placeholder = { Text("YYYY-MM-DD", fontSize = 11.sp) },
                            modifier = Modifier.weight(1.2f),
                            textStyle = TextStyle(fontSize = 12.sp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = textPrimary,
                                unfocusedTextColor = textPrimary,
                                focusedBorderColor = Color(0xFFFF7A00),
                                unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                                focusedLabelColor = Color(0xFFFF7A00),
                                unfocusedLabelColor = textSecondary
                            ),
                            singleLine = true
                        )

                        // Shift Toggle Buttons
                        Row(
                            modifier = Modifier.weight(1.5f),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Morning", "Evening").forEach { shift ->
                                val isSelected = selectedShiftState == shift
                                Button(
                                    onClick = { selectedShiftState = shift },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                    ),
                                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = if (shift == "Morning") Icons.Default.WbSunny else Icons.Default.NightsStay,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else textPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = shift,
                                        fontSize = 11.sp,
                                        color = if (isSelected) Color.White else textPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = textSecondary.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Select Student Status to Record:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (filteredStudents.isEmpty()) {
                        Text(
                            text = "No students enrolled in Springfield Academy yet.",
                            fontSize = 11.sp,
                            color = textSecondary,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    } else {
                        filteredStudents.forEach { s ->
                            val currentRec = allAttendance.find {
                                it.registerNumber == s.registerNumber &&
                                it.date == selectedAttendanceDate &&
                                it.shift == selectedShiftState
                            }
                            val currentStatus = currentRec?.status ?: "No Record"

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                                    .background(
                                        if (isDark) Color(0xFF0F172A).copy(0.4f) else Color(0xFFF8FAFC),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1.1f)) {
                                    Text(
                                        text = s.name,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimary
                                    )
                                    Text(
                                        text = "Reg: ${s.registerNumber} | Batch: ${s.batch}",
                                        fontSize = 10.sp,
                                        color = textSecondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Current: $currentStatus",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = when (currentStatus) {
                                            "Present" -> Color(0xFF10B981)
                                            "Absent" -> Color(0xFFEF4444)
                                            "Late" -> Color(0xFFF59E0B)
                                            else -> textSecondary
                                        }
                                    )
                                }

                                // Status action buttons
                                Row(
                                    modifier = Modifier.weight(1.4f),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    listOf(
                                        "Present" to Color(0xFF10B981),
                                        "Absent" to Color(0xFFEF4444),
                                        "Late" to Color(0xFFF59E0B)
                                    ).forEach { (statusOpt, color) ->
                                        val isSelected = currentStatus == statusOpt
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) color else color.copy(alpha = 0.08f))
                                                .clickable {
                                                    viewModel.markAttendance(
                                                        registerNumber = s.registerNumber,
                                                        date = selectedAttendanceDate,
                                                        shift = selectedShiftState,
                                                        status = statusOpt
                                                    )
                                                }
                                                .padding(vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = statusOpt,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color.White else color
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Student Absence report tracker
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.5.dp, cardBorder),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚨 DAILY ABSENT STUDENT LOGS ($todayStr)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val todayAbsences = filteredAttendance.filter { it.date == todayStr && it.status == "Absent" }
                    if (todayAbsences.isEmpty()) {
                        Text("Excellent! No students marked absent in morning/evening shifts today.", fontSize = 11.sp, color = textSecondary)
                    } else {
                        todayAbsences.forEach { ab ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val sName = filteredStudents.find { it.registerNumber == ab.registerNumber }?.name ?: ab.registerNumber
                                Text(sName, color = textPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Shift: ${ab.shift}", color = Color(0xFFEF4444), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Leave Approvals List
            Text("Pending Absence / Leave Requests", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)

            val pendingLeaves = filteredLeaves.filter { it.status == "Pending" }
            if (pendingLeaves.isEmpty()) {
                Text("All leave applications reviewed! Good job.", color = textSecondary, fontSize = 12.sp)
            } else {
                pendingLeaves.forEach { leave ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = BorderStroke(1.5.dp, cardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(leave.studentName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    Text("Reg: ${leave.studentRegister}", fontSize = 11.sp, color = textSecondary)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(accentColor.copy(0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("${leave.startDate} to ${leave.endDate}", color = accentColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Reason: ${leave.reason}", fontSize = 12.sp, color = textPrimary)
                            Text("Proof: ${leave.proofName}", fontSize = 11.sp, color = Color(0xFF10B981))

                            Spacer(modifier = Modifier.height(12.dp))

                            // Approve/Reject Controls
                            if (selectedLeaveForRemark?.id == leave.id) {
                                OutlinedTextField(
                                    value = coachRemarks,
                                    onValueChange = { coachRemarks = it },
                                    placeholder = { Text("Add comments or remarks to student...", color = textSecondary.copy(0.6f), fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                                        focusedBorderColor = Color(0xFFFF7A00), unfocusedBorderColor = if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6),
                                        focusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB), unfocusedContainerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)
                                    )
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            viewModel.coachApproveLeave(leave.id, true, coachRemarks)
                                            selectedLeaveForRemark = null
                                            coachRemarks = ""
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Accept Approve", fontSize = 11.sp, color = Color.White)
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.coachApproveLeave(leave.id, false, coachRemarks)
                                            selectedLeaveForRemark = null
                                            coachRemarks = ""
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Reject", fontSize = 11.sp, color = Color.White)
                                    }
                                }
                            } else {
                                Button(
                                    onClick = {
                                        selectedLeaveForRemark = leave
                                        coachRemarks = ""
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00))
                                ) {
                                    Text("Decision / Remarks", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }

            // Wellness Alert Warnings Dashboard
            Text("🚨 Student Mental Health Wellness Alerts", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            if (criticalAlerts.isEmpty()) {
                Text("Healthy metrics. No student wellness parameters flagged critical.", color = textSecondary, fontSize = 12.sp)
            } else {
                criticalAlerts.distinctBy { it.registerNumber }.forEach { entry ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF291515) else Color(0xFFFFF3F3)),
                        border = BorderStroke(1.5.dp, if (isDark) Color(0xFFEF4444).copy(0.7f) else Color(0xFFFFAFA8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = "", tint = Color(0xFFEF4444), modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                val sName = filteredStudents.find { it.registerNumber == entry.registerNumber }?.name ?: entry.registerNumber
                                Text(sName, fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 13.sp)
                                Text("Sleep Level: ${entry.sleepHours}h", fontSize = 11.sp, color = if (isDark) Color(0xFFFFAFA8) else Color(0xFFB91C1C))
                                Text("Mood: ${entry.mood} | Notes: ${entry.notes}", fontSize = 11.sp, color = textSecondary, lineHeight = 15.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Wellness Metrics Directory (Sleep hours, Breakfast, Lunch, Dinner, Water intake, Energy level, Mood, Notes, Improvements)
            Text("📋 Student Wellness & Meal Logs Directory", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            var expandedCoachStudentId by remember { mutableStateOf<String?>(null) }
            filteredStudents.forEach { s ->
                val isSelected = expandedCoachStudentId == s.registerNumber
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    border = BorderStroke(1.5.dp, cardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedCoachStudentId = if (isSelected) null else s.registerNumber },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(s.name, fontWeight = FontWeight.Bold, color = textPrimary, fontSize = 13.sp)
                                Text("Reg: ${s.registerNumber} | Batch: ${s.batch}", fontSize = 11.sp, color = textSecondary)
                            }
                            Icon(
                                imageVector = if (isSelected) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Expand Status",
                                tint = accentColor
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = textSecondary.copy(alpha = 0.2f))
                            Spacer(modifier = Modifier.height(8.dp))

                            // Latest Wellness Entry for student:
                            val latestLog = allWellness.filter { it.registerNumber == s.registerNumber }
                                .maxByOrNull { it.date } // get the newest entry

                            if (latestLog == null) {
                                Text("No daily wellness logs posted by this student yet.", fontSize = 11.sp, color = textSecondary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            } else {
                                Text("Latest Logs Record: ${latestLog.date}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor, modifier = Modifier.padding(bottom = 6.dp))

                                // Let's format and style the parameters requested:
                                // Sleep hours, Breakfast, Lunch, Dinner, Water intake, Energy level, Mood, Notes, Improvements
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // 1. Sleep hours
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Bedtime, contentDescription = "Sleep", tint = Color(0xFF38BDF8), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Sleep Quantity: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                        Text("${latestLog.sleepHours} Hours", fontSize = 11.sp, color = textPrimary)
                                    }

                                    // 2. Breakfast, Lunch, Dinner
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Restaurant, contentDescription = "Meals", tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Daily Meals Intake: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                    }
                                    Column(modifier = Modifier.padding(start = 24.dp)) {
                                        Text("🍳 Breakfast: ${if (latestLog.hadBreakfast) "Had Breakfast" else "Skipped"}", fontSize = 11.sp, color = textPrimary)
                                        Text("🍱 Lunch: ${if (latestLog.hadLunch) "Had Lunch" else "Skipped"}", fontSize = 11.sp, color = textPrimary)
                                        Text("🍜 Dinner: ${if (latestLog.hadDinner) "Had Dinner" else "Skipped"}", fontSize = 11.sp, color = textPrimary)
                                    }

                                    // 3. Water Intake
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.LocalCafe, contentDescription = "Water", tint = Color(0xFF60A5FA), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Water Quantity: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                        Text("${String.format(Locale.getDefault(), "%.1f", latestLog.waterIntakeCups * 0.25f)} L", fontSize = 11.sp, color = textPrimary)
                                    }

                                    // 5. Mood
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Mood, contentDescription = "Mood", tint = Color(0xFFEC4899), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Mood Status: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                        Text(latestLog.mood, fontSize = 11.sp, color = textPrimary)
                                    }

                                    // 6. Notes
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(Icons.AutoMirrored.Filled.Notes, contentDescription = "Notes", tint = Color(0xFFA78BFA), modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text("Personal Notes: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                            Text(if (latestLog.notes.trim().isEmpty()) "None recorded" else latestLog.notes, fontSize = 11.sp, color = textPrimary, lineHeight = 15.sp)
                                        }
                                    }

                                    // 7. Improvements
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(Icons.Default.TrendingUp, contentDescription = "Improvements", tint = Color(0xFF34D399), modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text("Aspirations / Improvements: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                            Text(if (latestLog.improvements.trim().isEmpty()) "None recorded" else latestLog.improvements, fontSize = 11.sp, color = textPrimary, lineHeight = 15.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            // -------------------------------------------------------------
            // A. UPCOMING TOURNAMENTS (Coach scheduler)
            // -------------------------------------------------------------
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.5.dp, cardBorder),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🏆 Upcoming Tournaments Schedule",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = "Schedule matches, track venues and assign target rosters.",
                        fontSize = 11.sp,
                        color = textSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Form to add tournament
                    var tourTitle by remember { mutableStateOf("") }
                    var tourDate by remember { mutableStateOf("") }
                    var tourLoc by remember { mutableStateOf("") }
                    var tournaments by remember { mutableStateOf(listOf(
                        CustomTournament("District Junior Athletics Meet", "2026-06-28", "Stadium Grounds A"),
                        CustomTournament("National Selection Volleyball Trials", "2026-07-15", "Indoor Sports Pavilion"),
                        CustomTournament("State Level Track & Field Trophy", "2026-07-29", "University Complex")
                    )) }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF130A04) else Color(0xFFFFFDFB)),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF422E1A) else Color(0xFFFFDFC6)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Assign New Tournament Profile:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            OutlinedTextField(
                                value = tourTitle,
                                onValueChange = { tourTitle = it },
                                placeholder = { Text("Title e.g. State Volleyball Cup", color = textSecondary, fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = TextStyle(fontSize = 11.sp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                                    focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
                                )
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = tourDate,
                                    onValueChange = { tourDate = it },
                                    placeholder = { Text("YYYY-MM-DD", color = textSecondary, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1f),
                                    textStyle = TextStyle(fontSize = 11.sp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                                        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
                                    )
                                )
                                OutlinedTextField(
                                    value = tourLoc,
                                    onValueChange = { tourLoc = it },
                                    placeholder = { Text("Venue", color = textSecondary, fontSize = 11.sp) },
                                    modifier = Modifier.weight(1.2f),
                                    textStyle = TextStyle(fontSize = 11.sp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = textPrimary, unfocusedTextColor = textPrimary,
                                        focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent
                                    )
                                )
                            }
                            Button(
                                onClick = {
                                    if (tourTitle.trim().isNotEmpty() && tourDate.trim().isNotEmpty()) {
                                        tournaments = tournaments + CustomTournament(tourTitle, tourDate, tourLoc)
                                        tourTitle = ""
                                        tourDate = ""
                                        tourLoc = ""
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Publish Tournament Roster", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    tournaments.forEach { tour ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .background(if (isDark) Color(0xFF0F172A).copy(0.4f) else Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(tour.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 2.dp)) {
                                    Icon(Icons.Default.Place, contentDescription = "", tint = accentColor, modifier = Modifier.size(11.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(tour.location, fontSize = 10.sp, color = textSecondary)
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(accentColor.copy(0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(tour.date, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // -------------------------------------------------------------
            // B. DOCUMENT UPDATION HUB (Coach student document checker)
            // -------------------------------------------------------------
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = BorderStroke(1.5.dp, cardBorder),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📂 Student Document Updation Columns",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Text(
                        text = "Review eligibility, birth certificates, medical sheets and update statuses.",
                        fontSize = 11.sp,
                        color = textSecondary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    var docStatusMap by remember { mutableStateOf(mapOf(
                        "2026CS501" to mapOf("Birth Certificate" to "Verified", "Medical Form" to "Submitted", "Consent Slip" to "Verified"),
                        "2026CS502" to mapOf("Birth Certificate" to "Submitted", "Medical Form" to "Pending Updation", "Consent Slip" to "Pending Updation"),
                        "2026CS503" to mapOf("Birth Certificate" to "Verified", "Medical Form" to "Verified", "Consent Slip" to "Verified")
                    )) }

                    var showDocSelectorForStudentId by remember { mutableStateOf<String?>(null) }
                    var showDocSelectorField by remember { mutableStateOf("") }

                    filteredStudents.forEach { std ->
                        val studentDocs = docStatusMap[std.registerNumber] ?: mapOf(
                            "Birth Certificate" to "Pending Updation",
                            "Medical Form" to "Pending Updation",
                            "Consent Slip" to "Pending Updation"
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .background(if (isDark) Color(0xFF0F172A).copy(0.4f) else Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(std.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                Text("Register: ${std.registerNumber}", fontSize = 10.sp, color = textSecondary)
                                
                                Spacer(modifier = Modifier.height(6.dp))
                                
                                // Document updation columns
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    studentDocs.forEach { (docType, status) ->
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(
                                                    when (status) {
                                                        "Verified" -> Color(0xFF10B981).copy(0.12f)
                                                        "Submitted" -> Color(0xFF3B82F6).copy(0.12f)
                                                        else -> Color(0xFFEF4444).copy(0.12f)
                                                    }
                                                )
                                                .clickable {
                                                    showDocSelectorForStudentId = std.registerNumber
                                                    showDocSelectorField = docType
                                                }
                                                .padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(docType, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                            Text(
                                                text = status,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = when (status) {
                                                    "Verified" -> Color(0xFF10B981)
                                                    "Submitted" -> Color(0xFF3B82F6)
                                                    else -> Color(0xFFEF4444)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Dialog or interactive selector to change any status
                    if (showDocSelectorForStudentId != null) {
                        val activeStudentId = showDocSelectorForStudentId!!
                        val activeDoc = showDocSelectorField
                        val stdName = filteredStudents.find { it.registerNumber == activeStudentId }?.name ?: activeStudentId
                        val currentStatus = (docStatusMap[activeStudentId] ?: emptyMap())[activeDoc] ?: "Pending Updation"

                        AlertDialog(
                            onDismissRequest = { showDocSelectorForStudentId = null },
                            title = { Text("Update Document Status", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
                            text = {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("Student: $stdName", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("Document: $activeDoc (Currently *${currentStatus}*)", fontSize = 11.sp, color = textSecondary)
                                    
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("Select New Status:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    
                                    listOf("Verified", "Submitted", "Pending Updation").forEach { statusOpt ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    val currentDocsMap = docStatusMap[activeStudentId]?.toMutableMap() ?: mutableMapOf()
                                                    currentDocsMap[activeDoc] = statusOpt
                                                    val updatedMap = docStatusMap.toMutableMap()
                                                    updatedMap[activeStudentId] = currentDocsMap
                                                    docStatusMap = updatedMap
                                                    showDocSelectorForStudentId = null
                                                }
                                                .padding(vertical = 10.dp, horizontal = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(statusOpt, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textPrimary)
                                            if (currentStatus == statusOpt) {
                                                Icon(Icons.Default.Check, contentDescription = "", tint = accentColor, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            },
                            confirmButton = {
                                TextButton(onClick = { showDocSelectorForStudentId = null }) {
                                    Text("Cancel", fontSize = 12.sp)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ==========================================================
// 5. ADMIN PORTAL LAYOUT
// ==========================================================
@Composable
fun AdminDashboardLayout(
    viewModel: AppViewModel,
    state: AuthState.Authenticated,
    students: List<StudentProfile>,
    allAttendance: List<AttendanceRecord>,
    allLeaves: List<LeaveApplication>,
    allWellness: List<WellnessEntry>,
    allFees: List<StudentFee>,
    allOrganizations: List<Organization>
) {
    var activeTab by remember { mutableStateOf("ANALYTICS") } // ANALYTICS, MANAGE_STUDENTS
    val isDark by viewModel.isDarkMode.collectAsState()

    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val topBarBg = if (isDark) Color(0xFF1E1107) else Color(0xFFFFF0E6)
    val accentColor = Color(0xFFFF7A00)

    val adminAcademy = state.academyName
    val filteredStudents = students.filter { it.academyName == adminAcademy }
    val academyRegs = filteredStudents.map { it.registerNumber }.toSet()
    
    val filteredAttendance = allAttendance.filter { it.registerNumber in academyRegs }
    val filteredLeaves = allLeaves.filter { it.studentRegister in academyRegs }
    val filteredWellness = allWellness.filter { it.registerNumber in academyRegs }
    val filteredFees = allFees.filter { it.studentRegister in academyRegs }
    val filteredOrganizations = allOrganizations.filter { it.organizationName == adminAcademy }

    // Admin Enrollment Forms
    var registerNum by remember { mutableStateOf("") }
    var enrollmentName by remember { mutableStateOf("") }
    var addressStr by remember { mutableStateOf("") }
    var mobileNo by remember { mutableStateOf("") }
    var parentNo by remember { mutableStateOf("") }
    var batchStr by remember { mutableStateOf("") }
    var courseStr by remember { mutableStateOf("") }
    var successToast by remember { mutableStateOf("") }

    // Coach and Subtab states
    var enrollmentSubTab by remember { mutableStateOf("STUDENTS") } // "STUDENTS" or "COACHES"
    var coachName by remember { mutableStateOf("") }
    var coachUsername by remember { mutableStateOf("") }
    var coachPass by remember { mutableStateOf("") }
    var coachSpecialty by remember { mutableStateOf("") }
    var coachSuccessToast by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(topBarBg)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .windowInsetsPadding(WindowInsets.statusBars)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Central Admin Controls", color = textPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(text = "System Enrollment & Metric Auditing", color = accentColor, fontSize = 11.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            var showRoleMenu by remember { mutableStateOf(false) }
                            IconButton(onClick = { showRoleMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.ManageAccounts,
                                    contentDescription = "Switch Portal Role",
                                    tint = accentColor
                                )
                            }
                            IconButton(onClick = { viewModel.logout() }) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit", tint = Color(0xFFEF4444))
                            }

                            if (showRoleMenu) {
                                AlertDialog(
                                    onDismissRequest = { showRoleMenu = false },
                                    title = { Text("Switch Portal Role", fontWeight = FontWeight.Bold, color = Color.White) },
                                    text = {
                                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Text("Select which dashboard/role you would like to test or switch into:", fontSize = 13.sp, color = Color.LightGray)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            listOf(
                                                "STUDENT" to "🎓 Student Portal",
                                                "COACH" to "⚽ Coach / Staff Portal",
                                                "ADMIN" to "⚙️ Central Admin Portal"
                                            ).forEach { (roleCode, label) ->
                                                Button(
                                                    onClick = {
                                                        viewModel.forceSwitchRole(roleCode)
                                                        showRoleMenu = false
                                                    },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    colors = ButtonDefaults.buttonColors(
                                                        containerColor = if (state.role == roleCode) Color(0xFFE67E22) else Color(0xFF334155)
                                                    )
                                                ) {
                                                    Text(label, color = Color.White, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                    },
                                    confirmButton = {},
                                    dismissButton = {
                                        TextButton(onClick = { showRoleMenu = false }) {
                                            Text("Cancel", color = Color(0xFFE67E22))
                                        }
                                    },
                                    containerColor = Color(0xFF1E293B)
                                )
                            }
                        }
                    }
                }

                TabRow(
                    selectedTabIndex = when (activeTab) {
                        "ANALYTICS" -> 0
                        "MANAGE_STUDENTS" -> 1
                        "SUBSCRIPTION_BILLING" -> 2
                        else -> 0
                    },
                    containerColor = topBarBg,
                    contentColor = accentColor
                ) {
                    Tab(
                        selected = activeTab == "ANALYTICS",
                        onClick = { activeTab = "ANALYTICS" },
                        text = { Text("Analytics", fontSize = 10.sp, color = textPrimary, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = activeTab == "MANAGE_STUDENTS",
                        onClick = { activeTab = "MANAGE_STUDENTS" },
                        text = { Text("Enroll", fontSize = 10.sp, color = textPrimary, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = activeTab == "SUBSCRIPTION_BILLING",
                        onClick = { activeTab = "SUBSCRIPTION_BILLING" },
                        text = { Text("Billing Console", fontSize = 10.sp, color = textPrimary, fontWeight = FontWeight.Bold) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                "ANALYTICS" -> {
                    AdminAnalyticsTab(
                        students = filteredStudents,
                        attendance = filteredAttendance,
                        wellness = filteredWellness,
                        isDark = isDark
                    )
                }
                "MANAGE_STUDENTS" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Subtab header to switch enrollment modes
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { enrollmentSubTab = "STUDENTS" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (enrollmentSubTab == "STUDENTS") accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Enrolled Students", color = if (enrollmentSubTab == "STUDENTS") Color.White else textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { enrollmentSubTab = "COACHES" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (enrollmentSubTab == "COACHES") accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Coaches & Access", color = if (enrollmentSubTab == "COACHES") Color.White else textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        if (enrollmentSubTab == "STUDENTS") {
                            // Student enrollment form & database (Scrollable container inside column)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text("Add New Student Profile", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                                OutlinedTextField(value = registerNum, onValueChange = { registerNum = it }, placeholder = { Text("Reg Number e.g. 2026CS509", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                OutlinedTextField(value = enrollmentName, onValueChange = { enrollmentName = it }, placeholder = { Text("Full Name", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                OutlinedTextField(value = addressStr, onValueChange = { addressStr = it }, placeholder = { Text("Local Address", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(value = mobileNo, onValueChange = { mobileNo = it }, placeholder = { Text("Mobile Phone", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                    OutlinedTextField(value = parentNo, onValueChange = { parentNo = it }, placeholder = { Text("Parent Phone", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(value = batchStr, onValueChange = { batchStr = it }, placeholder = { Text("Batch e.g. CS-A", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                    OutlinedTextField(value = courseStr, onValueChange = { courseStr = it }, placeholder = { Text("Course Program", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                }

                                if (successToast.isNotBlank()) {
                                    Text(successToast, color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (registerNum.isNotBlank() && enrollmentName.isNotBlank() && mobileNo.isNotBlank()) {
                                            val nProfile = StudentProfile(
                                                registerNumber = registerNum,
                                                name = enrollmentName,
                                                address = addressStr,
                                                mobileNumber = mobileNo,
                                                parentMobile = parentNo,
                                                batch = batchStr,
                                                course = courseStr,
                                                profilePhoto = "avatar_1",
                                                academyName = adminAcademy
                                            )
                                            viewModel.saveStudentProfile(nProfile)
                                            viewModel.createOneTimeAccount(
                                                phone = mobileNo.trim(),
                                                pass = "password123",
                                                role = "STUDENT",
                                                regNo = registerNum,
                                                academyName = adminAcademy
                                            ) { _, _ -> }
                                            
                                            successToast = "Successfully enrolled student record & auto-provisioned password123 credentials: $registerNum"
                                            registerNum = ""
                                            enrollmentName = ""
                                            addressStr = ""
                                            mobileNo = ""
                                            parentNo = ""
                                            batchStr = ""
                                            courseStr = ""
                                        } else {
                                            successToast = "Validation failed. Register, Name, and Mobile are required fields."
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                                ) {
                                    Text("Register Student", fontWeight = FontWeight.Bold, color = Color.White)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // List all registered profiles
                                var expandedStudentId by remember { mutableStateOf<String?>(null) }
                                Text("Active Profiles Database (${students.size} Students - Click to Inspect)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                students.forEach { s ->
                            val isSelected = expandedStudentId == s.registerNumber
                            Card(
                                colors = CardDefaults.cardColors(containerColor = cardBg),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        expandedStudentId = if (isSelected) null else s.registerNumber
                                    },
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(s.name, color = textPrimary, fontWeight = FontWeight.Bold)
                                            Text("Reg: ${s.registerNumber} | Course: ${s.course}", fontSize = 11.sp, color = textSecondary)
                                            Text("Mobile: ${s.mobileNumber} | Parent: ${s.parentMobile}", fontSize = 10.sp, color = textSecondary.copy(alpha = 0.8f))
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFF1ABC9C).copy(0.15f))
                                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                        ) {
                                            Text(s.batch, color = Color(0xFF1ABC9C), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    if (isSelected) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 14.dp),
                                            color = textSecondary.copy(alpha = 0.2f)
                                        )
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp)
                                        ) {
                                            // Address Info
                                            Text("📍 Residential Address/Hostel", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor)
                                            Text(s.address.ifEmpty { "Not specified" }, color = textPrimary, fontSize = 11.sp)
                                            Spacer(modifier = Modifier.height(8.dp))

                                            // 1. Attendance Records Summary
                                            val sAtt = allAttendance.filter { it.registerNumber == s.registerNumber }
                                            Text("📅 Attendance History (${sAtt.size} entries)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor)
                                            if (sAtt.isEmpty()) {
                                                Text("No attendance recorded yet", fontSize = 10.sp, color = textSecondary)
                                            } else {
                                                Column(modifier = Modifier.padding(start = 6.dp, top = 2.dp)) {
                                                    sAtt.take(5).forEach { record ->
                                                        val colorPill = when (record.status.uppercase(Locale.US)) {
                                                            "PRESENT" -> Color(0xFF10B981)
                                                            "LATE" -> Color(0xFFF59E0B)
                                                            else -> Color(0xFFEF4444)
                                                        }
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                                            horizontalArrangement = Arrangement.SpaceBetween,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text("${record.date} [Shift: ${record.shift}]", fontSize = 10.sp, color = textPrimary)
                                                            Box(
                                                                modifier = Modifier
                                                                    .clip(RoundedCornerShape(4.dp))
                                                                    .background(colorPill.copy(0.15f))
                                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                                            ) {
                                                                Text(record.status, color = colorPill, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                                                            }
                                                        }
                                                    }
                                                    if (sAtt.size > 5) {
                                                        Text("...and ${sAtt.size - 5} more entries", fontSize = 9.sp, color = textSecondary)
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // 2. Wellness Metrics History (Sleep, Breakfast, Lunch, Dinner, Water, Energy, Mood, Improvements, Notes)
                                            val sWell = allWellness.filter { it.registerNumber == s.registerNumber }
                                            Text("❤️ Wellness Metrics History (${sWell.size} logs)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor)
                                            if (sWell.isEmpty()) {
                                                Text("No wellness records updated yet", fontSize = 10.sp, color = textSecondary)
                                            } else {
                                                Column(modifier = Modifier.padding(start = 6.dp, top = 2.dp)) {
                                                    sWell.take(3).forEach { record ->
                                                        Card(
                                                            colors = CardDefaults.cardColors(containerColor = (if (isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9))),
                                                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                                        ) {
                                                            Column(modifier = Modifier.padding(8.dp)) {
                                                                Text("Date: ${record.date}", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = textSecondary)
                                                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                                                                    Text("💤 Sleep: ${record.sleepHours} hrs | 💧 Water: ${String.format(Locale.getDefault(), "%.1f", record.waterIntakeCups * 0.25f)} L", fontSize = 9.sp, color = textPrimary)
                                                                }
                                                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                                                                    Text("🍳 Meals: Breakfast: ${if (record.hadBreakfast) "Yes" else "No"} | Lunch: ${if (record.hadLunch) "Yes" else "No"} | Dinner: ${if (record.hadDinner) "Yes" else "No"}", fontSize = 9.sp, color = textPrimary)
                                                                }
                                                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                                                    Text("🧠 Mood: ${record.mood}", fontSize = 9.sp, color = textPrimary)
                                                                }
                                                                if (record.notes.isNotEmpty()) {
                                                                    Text("📝 Notes: ${record.notes}", fontSize = 9.sp, color = textPrimary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                                                                }
                                                                if (record.improvements.isNotEmpty()) {
                                                                    Text("🌟 Improvements: ${record.improvements}", fontSize = 9.sp, color = textPrimary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (sWell.size > 3) {
                                                        Text("...and ${sWell.size - 3} more days", fontSize = 9.sp, color = textSecondary)
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // 3. Leaves Log
                                            val sLeaves = allLeaves.filter { it.studentRegister == s.registerNumber }
                                            Text("✉️ Leave Applications (${sLeaves.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = accentColor)
                                            if (sLeaves.isEmpty()) {
                                                Text("No leave applied yet", fontSize = 10.sp, color = textSecondary)
                                            } else {
                                                Column(modifier = Modifier.padding(start = 6.dp, top = 2.dp)) {
                                                    sLeaves.forEach { leave ->
                                                        val statusColor = when (leave.status.uppercase(Locale.US)) {
                                                            "APPROVED" -> Color(0xFF10B981)
                                                            "REJECTED" -> Color(0xFFEF4444)
                                                            else -> Color(0xFFF59E0B)
                                                        }
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                                                            horizontalArrangement = Arrangement.SpaceBetween
                                                        ) {
                                                            Text("${leave.startDate} to ${leave.endDate} - Reason: ${leave.reason}", fontSize = 10.sp, color = textPrimary, modifier = Modifier.weight(0.7f))
                                                            Box(
                                                                modifier = Modifier
                                                                    .clip(RoundedCornerShape(4.dp))
                                                                    .background(statusColor.copy(0.15f))
                                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                                            ) {
                                                                Text(leave.status, color = statusColor, fontSize = 8.sp, fontWeight = FontWeight.ExtraBold)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                            // Coach Management System Tab
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text("Add Academy Coaching Staff", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)

                                OutlinedTextField(value = coachName, onValueChange = { coachName = it }, placeholder = { Text("Coach Full Name", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                OutlinedTextField(value = coachUsername, onValueChange = { coachUsername = it }, placeholder = { Text("Coach Username / Mobile Phone", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                OutlinedTextField(value = coachPass, onValueChange = { coachPass = it }, placeholder = { Text("Choose Login Password", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))
                                OutlinedTextField(value = coachSpecialty, onValueChange = { coachSpecialty = it }, placeholder = { Text("Coaching Specialty (e.g. Judo Instructor)", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = textPrimary, unfocusedTextColor = textPrimary, focusedBorderColor = accentColor))

                                if (coachSuccessToast.isNotBlank()) {
                                    Text(coachSuccessToast, color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        if (coachName.isNotBlank() && coachUsername.isNotBlank() && coachPass.isNotBlank()) {
                                            viewModel.addCoachDetails(
                                                name = coachName.trim(),
                                                username = coachUsername.trim(),
                                                pass = coachPass.trim(),
                                                specialty = coachSpecialty.ifEmpty { "Chief Coach" },
                                                academy = adminAcademy,
                                                hasAccess = true
                                            )
                                            coachSuccessToast = "Successfully registered Coach: $coachName with full portal authorization!"
                                            coachName = ""
                                            coachUsername = ""
                                            coachPass = ""
                                            coachSpecialty = ""
                                        } else {
                                            coachSuccessToast = "Name, Username, and Password are required fields."
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth().height(48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                                ) {
                                    Text("Add Coaching Specialist", fontWeight = FontWeight.Bold, color = Color.White)
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                val allCoaches by viewModel.allCoaches.collectAsState()
                                val academyCoaches = allCoaches.filter { it.academyName == adminAcademy }

                                Text("Academy Coaches & Portal Access (Total: ${academyCoaches.size})", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                if (academyCoaches.isEmpty()) {
                                    Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.fillMaxWidth()) {
                                        Text("No coaches enrolled for this academy.", modifier = Modifier.padding(16.dp), color = textSecondary, fontSize = 12.sp)
                                    }
                                } else {
                                    academyCoaches.forEach { coach ->
                                        Card(
                                            colors = CardDefaults.cardColors(containerColor = cardBg),
                                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(coach.name, color = textPrimary, fontWeight = FontWeight.Bold)
                                                    Text("Username: ${coach.username} | Specialization: ${coach.specialty}", fontSize = 11.sp, color = textSecondary)
                                                    Text(
                                                        text = if (coach.hasAccess) "🟢 PORTAL ACCESS ACTIVE" else "🔴 PORTAL BLOCKED",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (coach.hasAccess) Color(0xFF10B981) else Color(0xFFEF4444)
                                                    )
                                                }
                                                Switch(
                                                    checked = coach.hasAccess,
                                                    onCheckedChange = { isChecked ->
                                                        viewModel.updateCoachAccess(coach, isChecked)
                                                    },
                                                    colors = SwitchDefaults.colors(
                                                        checkedThumbColor = Color.White,
                                                        checkedTrackColor = accentColor
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "SUBSCRIPTION_BILLING" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        var billingSubTab by remember { mutableStateOf("STUDENTS") } // "STUDENTS" or "PLATFORM"
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { billingSubTab = "STUDENTS" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (billingSubTab == "STUDENTS") accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Student Tuition Fees", color = if (billingSubTab == "STUDENTS") Color.White else textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { billingSubTab = "PLATFORM" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (billingSubTab == "PLATFORM") accentColor else (if (isDark) Color(0xFF2E190A) else Color(0xFFFFDFC6))
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Platform SaaS Plan", color = if (billingSubTab == "PLATFORM") Color.White else textPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        if (billingSubTab == "STUDENTS") {
                            AdminFeesManagementTab(
                                viewModel = viewModel,
                                students = filteredStudents,
                                allFees = filteredFees,
                                isDark = isDark,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            AdminSubscriptionBillingTab(
                                viewModel = viewModel,
                                students = filteredStudents,
                                allOrganizations = filteredOrganizations,
                                isDark = isDark,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminFeesManagementTab(
    viewModel: AppViewModel,
    students: List<StudentProfile>,
    allFees: List<StudentFee>,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val accentColor = Color(0xFFFF7A00)

    var selectedStatusFilter by remember { mutableStateOf("All") } // All, Paid, Pending, Overdue
    var searchQuery by remember { mutableStateOf("") }

    // Forms for assigning/adding fee
    var showAssignDialog by remember { mutableStateOf(false) }
    var assignStudentRegister by remember { mutableStateOf("") }
    var assignMonth by remember { mutableStateOf("May") }
    var assignYear by remember { mutableStateOf(2026) }
    var assignAmount by remember { mutableStateOf("1500") }
    var assignStatus by remember { mutableStateOf("Pending") }

    // Edit state
    var showEditDialog by remember { mutableStateOf<StudentFee?>(null) }
    var editStatus by remember { mutableStateOf("Paid") }
    var editMode by remember { mutableStateOf("UPI") } // UPI, Cash, Bank Transfer
    var editRef by remember { mutableStateOf("") }
    var editRemarks by remember { mutableStateOf("") }

    val modeList = listOf("UPI", "Cash", "Bank Transfer")

    // Filter Logic
    val filteredFees = allFees.filter { fee ->
        val matchesStatus = selectedStatusFilter == "All" || fee.status.equals(selectedStatusFilter, ignoreCase = true)
        val studentName = students.find { it.registerNumber == fee.studentRegister }?.name ?: ""
        val matchesSearch = fee.studentRegister.contains(searchQuery, ignoreCase = true) || 
                            studentName.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesSearch
    }

    // Calculations for metrics cards
    val totalStudents = students.size
    val paidCount = allFees.count { it.status.equals("Paid", ignoreCase = true) }
    val pendingCount = allFees.count { it.status.equals("Pending", ignoreCase = true) }
    val overdueCount = allFees.count { it.status.equals("Overdue", ignoreCase = true) }
    val totalCollection = allFees.filter { it.status.equals("Paid", ignoreCase = true) }.sumOf { it.amount }
    val totalAmountInvoice = allFees.sumOf { it.amount }.coerceAtLeast(1.0)
    val collectionPercentage = ((totalCollection / totalAmountInvoice) * 100).toInt()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Fee Collection Dashboard", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
            Button(
                onClick = {
                    if (students.isNotEmpty()) {
                        assignStudentRegister = students.first().registerNumber
                    }
                    showAssignDialog = true
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Assign Fee", tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Assign Fee", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        // Metrics Grid
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                // Metric 1
                Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Total Studs", fontSize = 10.sp, color = textSecondary)
                        Text("$totalStudents", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                }
                // Metric 2
                Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Paid Status", fontSize = 10.sp, color = textSecondary)
                        Text("$paidCount Paid", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22C55E))
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                // Metric 3
                Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Pending / Overdue", fontSize = 10.sp, color = textSecondary)
                        Text("$pendingCount / $overdueCount", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                    }
                }
                // Metric 4
                Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Revenue (Collection)", fontSize = 10.sp, color = textSecondary)
                        Text("₹${totalCollection.toInt()} ($collectionPercentage%)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                }
            }
        }

        // Filters UI
        Card(colors = CardDefaults.cardColors(containerColor = cardBg), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Search & Filter Payments", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textSecondary)

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search by Reg No or Name...", fontSize = 11.sp, color = textSecondary.copy(0.6f)) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    textStyle = TextStyle(fontSize = 11.sp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = textSecondary.copy(0.3f)
                    ),
                    singleLine = true
                )

                // Row of filter chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("All", "Paid", "Pending", "Overdue").forEach { status ->
                        val isSelected = selectedStatusFilter == status
                        val chipBg = if (isSelected) accentColor else textSecondary.copy(0.12f)
                        val chipText = if (isSelected) Color.White else textPrimary
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(chipBg)
                                .clickable { selectedStatusFilter = status }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(status, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = chipText)
                        }
                    }
                }
            }
        }

        // List of invoices
        Text("Payments History Ledger (${filteredFees.size} records)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
        if (filteredFees.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("No matching ledger records found", fontSize = 11.sp, color = textSecondary)
            }
        } else {
            filteredFees.forEach { fee ->
                val sName = students.find { it.registerNumber == fee.studentRegister }?.name ?: "Unknown Student"
                val statusColor = when (fee.status.uppercase(Locale.US)) {
                    "PAID" -> Color(0xFF22C55E)
                    "PENDING" -> Color(0xFFF59E0B)
                    else -> Color(0xFFEF4444)
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(sName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                Text("Reg: ${fee.studentRegister} | Cycle: ${fee.month} ${fee.year}", fontSize = 10.sp, color = textSecondary)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(statusColor.copy(0.15f))
                                    .clickable {
                                        editStatus = fee.status
                                        editMode = if (fee.paymentMode.isNotEmpty()) fee.paymentMode else "UPI"
                                        editRef = fee.transactionReference
                                        editRemarks = fee.remarks
                                        showEditDialog = fee
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(fee.status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.ExtraBold)
                            }
                        }

                        HorizontalDivider(color = textSecondary.copy(0.1f), modifier = Modifier.padding(vertical = 4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Amount Assigned: ₹${fee.amount.toInt()}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                if (fee.paymentDate.isNotEmpty()) {
                                    Text("Paid Date: ${fee.paymentDate} via ${fee.paymentMode}", fontSize = 10.sp, color = textSecondary)
                                    if (fee.transactionReference.isNotEmpty()) {
                                        Text("Ref No: ${fee.transactionReference}", fontSize = 9.sp, color = textSecondary.copy(0.8f))
                                    }
                                } else {
                                    Text("Unpaid - Pending Settlement", fontSize = 10.sp, color = textSecondary.copy(0.8f))
                                }
                                if (fee.remarks.isNotEmpty()) {
                                    Text("Remarks: ${fee.remarks}", fontSize = 9.sp, color = textSecondary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (!fee.status.equals("Paid", ignoreCase = true)) {
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    IconButton(
                                        onClick = {
                                            val studentMobile = students.find { it.registerNumber == fee.studentRegister }?.mobileNumber ?: ""
                                            val cleanPhone = studentMobile.filter { it.isDigit() }
                                            val phoneWithCountry = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
                                            val reminderMessage = "Dear $sName,\n\nThis is a friendly reminder that your assigned school fee of *₹${fee.amount.toInt()}* for the billing period *${fee.month} ${fee.year}* remains *unpaid* (${fee.status}). Please arrange for the payment to be settled at the earliest convenience.\n\nThank you!"
                                            val whatsappUri = android.net.Uri.parse("https://api.whatsapp.com/send?phone=$phoneWithCountry&text=${android.net.Uri.encode(reminderMessage)}")
                                            val whatsappIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, whatsappUri)
                                            try {
                                                context.startActivity(whatsappIntent)
                                            } catch (e: Exception) {
                                                android.widget.Toast.makeText(context, "WhatsApp not installed. Launching web link...", android.widget.Toast.LENGTH_SHORT).show()
                                                try {
                                                    val browserIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, whatsappUri)
                                                    context.startActivity(browserIntent)
                                                } catch (ex: Exception) {
                                                    android.widget.Toast.makeText(context, "Could not open link", android.widget.Toast.LENGTH_LONG).show()
                                                }
                                            }
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Send,
                                            contentDescription = "Send WhatsApp Alert",
                                            tint = Color(0xFF25D366),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        editStatus = fee.status
                                        editMode = if (fee.paymentMode.isNotEmpty()) fee.paymentMode else "UPI"
                                        editRef = fee.transactionReference
                                        editRemarks = fee.remarks
                                        showEditDialog = fee
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Receipt", tint = accentColor, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Seeding Assign Fee Alert Dialog
        if (showAssignDialog) {
            AlertDialog(
                onDismissRequest = { showAssignDialog = false },
                title = { Text("Assign New Student Monthly Fee", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Select Enrolled Student Register Number:", fontSize = 10.sp, color = textSecondary)

                        OutlinedTextField(
                            value = assignStudentRegister,
                            onValueChange = { assignStudentRegister = it },
                            placeholder = { Text("e.g. 2026CS501", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            textStyle = TextStyle(fontSize = 11.sp)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = assignMonth,
                                onValueChange = { assignMonth = it },
                                placeholder = { Text("Month", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).height(48.dp),
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                            OutlinedTextField(
                                value = assignYear.toString(),
                                onValueChange = { assignYear = it.toIntOrNull() ?: 2026 },
                                placeholder = { Text("Year", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f).height(48.dp),
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                        }

                        OutlinedTextField(
                            value = assignAmount,
                            onValueChange = { assignAmount = it },
                            placeholder = { Text("Amount in ₹", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            textStyle = TextStyle(fontSize = 11.sp)
                        )

                        Text("Initial State Status:", fontSize = 10.sp, color = textSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Pending", "Paid", "Overdue").forEach { s ->
                                val isChosen = assignStatus == s
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isChosen) accentColor else textSecondary.copy(0.1f))
                                        .clickable { assignStatus = s }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(s, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isChosen) Color.White else textPrimary)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        onClick = {
                            if (assignStudentRegister.isNotBlank()) {
                                viewModel.recordFeePayment(
                                    studentRegister = assignStudentRegister.trim(),
                                    month = assignMonth,
                                    year = assignYear,
                                    amount = assignAmount.toDoubleOrNull() ?: 1500.0,
                                    status = assignStatus
                                )
                                showAssignDialog = false
                            }
                        }
                    ) {
                        Text("Record Asset", fontSize = 11.sp, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAssignDialog = false }) {
                        Text("Abort", fontSize = 11.sp, color = textSecondary)
                    }
                },
                containerColor = cardBg
            )
        }

        // Seeding Edit / Pay Alert Dialog
        showEditDialog?.let { currentFee ->
            AlertDialog(
                onDismissRequest = { showEditDialog = null },
                title = { Text("Audit / Settle Student Payment", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Settle fee billing receipt for student register ${currentFee.studentRegister}", fontSize = 11.sp, color = textSecondary)

                        Text("Settlement Status:", fontSize = 10.sp, color = textSecondary)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("Paid", "Pending", "Overdue").forEach { s ->
                                val isChosen = editStatus == s
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(if (isChosen) accentColor else textSecondary.copy(0.1f))
                                        .clickable { editStatus = s }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(s, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = if (isChosen) Color.White else textPrimary)
                                }
                            }
                        }

                        if (editStatus == "Paid") {
                            Text("Payment Mode Selection:", fontSize = 10.sp, color = textSecondary)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                modeList.forEach { m ->
                                    val isChosen = editMode == m
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isChosen) accentColor else textSecondary.copy(0.1f))
                                            .clickable { editMode = m }
                                            .padding(horizontal = 10.dp, vertical = 5.dp)
                                    ) {
                                        Text(m, fontSize = 9.sp, color = if (isChosen) Color.White else textPrimary)
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = editRef,
                                onValueChange = { editRef = it },
                                placeholder = { Text("UPI Txn Ref / Receipt No...", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                        }

                        OutlinedTextField(
                            value = editRemarks,
                            onValueChange = { editRemarks = it },
                            placeholder = { Text("Admin Audit remarks...", fontSize = 11.sp) },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            textStyle = TextStyle(fontSize = 11.sp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        onClick = {
                            val today = if (editStatus == "Paid") {
                                java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
                            } else ""

                            viewModel.updateFeeStatus(
                                currentFee.copy(
                                    status = editStatus,
                                    paymentDate = today,
                                    paymentMode = if (editStatus == "Paid") editMode else "",
                                    transactionReference = if (editStatus == "Paid") editRef else "",
                                    remarks = editRemarks
                                )
                            )
                            showEditDialog = null
                        }
                    ) {
                        Text("Commit Changes", fontSize = 11.sp, color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = null }) {
                        Text("Abort", fontSize = 11.sp, color = textSecondary)
                    }
                },
                containerColor = cardBg
            )
        }
    }
}

@Composable
fun AdminSubscriptionBillingTab(
    viewModel: AppViewModel,
    students: List<StudentProfile>,
    allOrganizations: List<Organization>,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val accentColor = Color(0xFFFF7A00)

    var showEditOrgDialog by remember { mutableStateOf<Organization?>(null) }
    var editOrgName by remember { mutableStateOf("") }
    var editOrgContact by remember { mutableStateOf("") }
    var editOrgMobile by remember { mutableStateOf("") }
    var editOrgEmail by remember { mutableStateOf("") }

    val defaultOrg = allOrganizations.firstOrNull() ?: Organization(
        organizationName = "Springfield Academy",
        contactPerson = "Principal Skinner",
        mobile = "9876543210",
        email = "skinner@springfield.edu",
        activeStudentCount = students.size,
        monthlyAmount = students.size * 100.0,
        status = "Active"
    )

    val currentStudentsCount = students.size
    val coroutineScope = rememberCoroutineScope()

    // Tier Plan management
    var selectedPlanName by remember(defaultOrg) {
        mutableStateOf(
            if (defaultOrg.subscriptionPlan.contains("Starter", ignoreCase = true)) "Starter Plan"
            else if (defaultOrg.subscriptionPlan.contains("Enterprise", ignoreCase = true) || defaultOrg.subscriptionPlan.contains("Per Student", ignoreCase = true)) "Enterprise Dynamic Plan"
            else "Professional Plan"
        )
    }

    // Dynamic License Price Calculation
    val basePlanAmount = when (selectedPlanName) {
        "Starter Plan" -> 1499.0
        "Professional Plan" -> 4999.0
        else -> currentStudentsCount * 100.0
    }
    
    val taxAmount = basePlanAmount * 0.18
    val grandTotalAmount = basePlanAmount + taxAmount

    // Simulated Subscription Flow state
    var isSubscriptionAutorenew by remember { mutableStateOf(true) }
    var currentPaidUntilDate by remember(defaultOrg) { 
        mutableStateOf(if (defaultOrg.subscriptionEndDate.isNotBlank()) defaultOrg.subscriptionEndDate else "June 30, 2026")
    }
    var currentSubscriptionStatus by remember(defaultOrg) {
        mutableStateOf(if (defaultOrg.status.isNotBlank()) defaultOrg.status else "Active")
    }

    // Toast Toast or Success Toast UI
    var successToastMessage by remember { mutableStateOf("") }

    // State for Payment Dialog
    var showPaymentDialog by remember { mutableStateOf(false) }
    var paymentMethod by remember { mutableStateOf("CARD") } // CARD or UPI

    // Input States for Card checkout form
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }

    // Input States for UPI checkout form
    var upiId by remember { mutableStateOf("") }

    // Payment Processing Loader State
    var isPaymentProcessing by remember { mutableStateOf(false) }
    var paymentProcessingStep by remember { mutableStateOf("") }
    var isPaymentSuccessState by remember { mutableStateOf(false) }

    // Stateful Billing Logs which prepends dynamically on successful payment capture
    val billingLogs = remember {
        mutableStateListOf(
            Triple("May 2026", "Professional Plan", "Awaiting Renewal on May 30th"),
            Triple("Apr 2026", "Professional Plan", "Paid & Settled - Ref: TXN-44919-X"),
            Triple("Mar 2026", "Professional Plan", "Paid & Settled - Ref: TXN-28311-K")
        )
    }

    if (successToastMessage.isNotBlank()) {
        LaunchedEffect(successToastMessage) {
            kotlinx.coroutines.delay(3500)
            successToastMessage = ""
        }
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Institution Subscription Billing", fontSize = 17.sp, fontWeight = FontWeight.Black, color = textPrimary)
            
            // Edit profile button on the header
            IconButton(onClick = {
                editOrgName = defaultOrg.organizationName
                editOrgContact = defaultOrg.contactPerson
                editOrgMobile = defaultOrg.mobile
                editOrgEmail = defaultOrg.email
                showEditOrgDialog = defaultOrg
            }) {
                Icon(Icons.Default.Settings, contentDescription = "Edit Profile", tint = accentColor)
            }
        }

        // Live Active Plan Card
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.dp, accentColor)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Subscription active indicator row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(defaultOrg.organizationName.ifBlank { "Springfield Academy" }, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text("Active Administrator License Client", fontSize = 11.sp, color = textSecondary)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (currentSubscriptionStatus == "Active") Color(0xFF10B981).copy(0.12f)
                                else Color(0xFFF97316).copy(0.12f)
                            )
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (currentSubscriptionStatus == "Active") Color(0xFF10B981) else Color(0xFFF97316))
                            )
                            Text(
                                text = currentSubscriptionStatus.uppercase(),
                                color = if (currentSubscriptionStatus == "Active") Color(0xFF10B981) else Color(0xFFF97316),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                HorizontalDivider(color = textSecondary.copy(alpha = 0.1f))

                // Detail Fields
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("ACTIVE LICENSE PLAN", fontSize = 10.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                        Text(selectedPlanName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("LICENSE VALID UNTIL", fontSize = 10.sp, color = textSecondary, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.DateRange, contentDescription = "", tint = accentColor, modifier = Modifier.size(14.dp))
                            Text(currentPaidUntilDate, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        }
                    }
                }

                // Auto renewal switch setting
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isDark) Color(0xFF10162B) else Color(0xFFF1F5F9))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "", tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                        Column {
                            Text("Auto-Renewal Safeguard", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("Drafts billing securely on date end", fontSize = 9.sp, color = textSecondary)
                        }
                    }
                    Switch(
                        checked = isSubscriptionAutorenew,
                        onCheckedChange = { isSubscriptionAutorenew = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = accentColor
                        )
                    )
                }

                // Interactive Renewal trigger
                Button(
                    onClick = {
                        isPaymentSuccessState = false
                        showPaymentDialog = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Icon(Icons.Default.Payment, contentDescription = "", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Secure Payment Gateway Checkout", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Custom Toast Box
        if (successToastMessage.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF10B981).copy(0.15f))
                    .border(1.dp, Color(0xFF10B981), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "", tint = Color(0xFF10B981))
                    Text(successToastMessage, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color(0xFF0F5132))
                }
            }
        }

        // Interactive Subscription Tier Configurator Selectors
        Text("🚀 Select & Configure SaaS Tiers", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            val tiers = listOf(
                Triple("Starter Plan", "₹1,499", "Up to 30 active student profiles standard logs"),
                Triple("Professional Plan", "₹4,999", "Up to 200 student profiles, full wellness insights (Most Common)"),
                Triple("Enterprise Dynamic Plan", "₹100/Student/Mo", "Pricing scales dynamic with database enrollment count")
            )

            tiers.forEach { (tierName, tierRate, tierDesc) ->
                val isSelected = selectedPlanName == tierName
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPlanName = tierName },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) accentColor.copy(0.08f) else cardBg
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) accentColor else (if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0))
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(tierName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                if (tierName == "Professional Plan") {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(accentColor)
                                            .padding(horizontal = 5.dp, vertical = 2.dp)
                                    ) {
                                        Text("POPULAR", color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.ExtraBold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(tierDesc, fontSize = 10.sp, color = textSecondary, lineHeight = 13.sp)
                        }
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                            Text(tierRate, fontSize = 14.sp, fontWeight = FontWeight.Black, color = if (isSelected) accentColor else textPrimary)
                            Text("monthly billing", fontSize = 8.sp, color = textSecondary)
                        }
                    }
                }
            }
        }

        // Live Itemized Calculations Card
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF10162B) else Color(0xFFF1F5F9)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📊 Monthly Calculation Invoice Breakdown", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Enrolled Database Actives", fontSize = 11.sp, color = textSecondary)
                    Text("$currentStudentsCount active students", fontSize = 11.sp, color = textPrimary, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Plan Selected Rate", fontSize = 11.sp, color = textSecondary)
                    Text(
                        text = if (selectedPlanName == "Enterprise Dynamic Plan") "₹100.00 / Student" else selectedPlanName,
                        fontSize = 11.sp,
                        color = textPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("SaaS License Subtotal", fontSize = 11.sp, color = textSecondary)
                    Text("₹${basePlanAmount.toInt()}.00", fontSize = 11.sp, color = textPrimary, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Cloud Infrastructure Service Levy (GST 18%)", fontSize = 11.sp, color = textSecondary)
                    Text("₹${taxAmount.toInt()}.00", fontSize = 11.sp, color = textPrimary)
                }

                HorizontalDivider(color = textSecondary.copy(0.1f), modifier = Modifier.padding(vertical = 4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Grand Monthly Invoice Total", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Text("₹${grandTotalAmount.toInt()}.00", fontSize = 14.sp, fontWeight = FontWeight.Black, color = accentColor)
                }
            }
        }

        // Historical Ledger Cycles
        Text("📅 Historical Billing Ledger Cycles", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textPrimary)

        billingLogs.forEach { bRecord ->
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isDark) Color(0xFF1E293B) else Color(0xFFE2E8F0))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(accentColor.copy(0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = "", tint = accentColor, modifier = Modifier.size(16.dp))
                        }
                        Column {
                            Text(bRecord.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                            Text("${bRecord.second} • ${bRecord.third}", fontSize = 10.sp, color = textSecondary)
                        }
                    }
                    val amountText = if (bRecord.second == "Starter Plan") "₹1,499"
                    else if (bRecord.second == "Professional Plan") "₹4,999"
                    else "₹${(currentStudentsCount * 100 * 1.18).toInt()}"
                    Text(
                        text = amountText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF10B981)
                    )
                }
            }
        }
    }

    // 1. PAYMENT GATEWAY SIMULATION CHECKOUT DIALOG
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { 
                if (!isPaymentProcessing) showPaymentDialog = false 
            },
            title = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Lock, contentDescription = "", tint = Color(0xFF10B981), modifier = Modifier.size(20.dp))
                    Text(
                        text = "SECURED STRIPE SaaS PORTAL", 
                        fontSize = 13.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textPrimary,
                        letterSpacing = 1.2.sp
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    
                    if (isPaymentProcessing) {
                        // Banking animation screen
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = accentColor,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            Text(
                                text = "Transaction in Progress",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = paymentProcessingStep,
                                fontSize = 11.sp,
                                color = textSecondary,
                                modifier = Modifier.padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "🔒 256-bit AES Certified Connection Network",
                                fontSize = 9.sp,
                                color = Color(0xFF10B981),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else if (isPaymentSuccessState) {
                        // Delightful checkout success check
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "PAYMENT SUCCESSFUL",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = textPrimary,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Authorized settlement completed securely. App access is fully upgraded.",
                                fontSize = 11.sp,
                                color = textSecondary,
                                modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Primary Payment inputs forms
                        Card(
                            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF10162B) else Color(0xFFF1F5F9)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Pay Licensing Total Fee", fontSize = 10.sp, color = textSecondary)
                                    Text("₹${grandTotalAmount.toInt()}.00", fontSize = 15.sp, fontWeight = FontWeight.Black, color = textPrimary)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(accentColor.copy(0.12f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(selectedPlanName, color = accentColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Gateway Toggle Hub (Credit Card OR UPI)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isDark) Color(0xFF10162B) else Color(0xFFF1F5F9)),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf("CARD", "UPI").forEach { method ->
                                val isSel = paymentMethod == method
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { paymentMethod = method }
                                        .background(if (isSel) accentColor else Color.Transparent)
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (method == "CARD") "💳 Credit/Debit Card" else "📱 UPI Address",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (isSel) Color.White else textSecondary
                                    )
                                }
                            }
                        }

                        if (paymentMethod == "CARD") {
                            // Credit Card Setup Form
                            OutlinedTextField(
                                value = cardNumber,
                                onValueChange = { input ->
                                    // Strip spaces and keep max digit size 16
                                    val digits = input.filter { it.isDigit() }.take(16)
                                    // format: e.g. "4111 2222 3333 4444"
                                    cardNumber = digits.chunked(4).joinToString(" ")
                                },
                                label = { Text("Card Number", fontSize = 11.sp) },
                                placeholder = { Text("4111 2222 3333 4444", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 11.sp, letterSpacing = 1.2.sp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = cardExpiry,
                                    onValueChange = { input ->
                                        val filtered = input.filter { it.isDigit() }.take(4)
                                        cardExpiry = if (filtered.length >= 3) {
                                            "${filtered.substring(0, 2)}/${filtered.substring(2)}"
                                        } else {
                                            filtered
                                        }
                                    },
                                    label = { Text("Expiry (MM/YY)", fontSize = 11.sp) },
                                    placeholder = { Text("12/28", fontSize = 11.sp) },
                                    modifier = Modifier.weight(1.2f),
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 11.sp)
                                )

                                OutlinedTextField(
                                    value = cardCvv,
                                    onValueChange = { input ->
                                        cardCvv = input.filter { it.isDigit() }.take(3)
                                    },
                                    label = { Text("CVV", fontSize = 11.sp) },
                                    placeholder = { Text("***", fontSize = 11.sp) },
                                    modifier = Modifier.weight(0.8f),
                                    singleLine = true,
                                    textStyle = TextStyle(fontSize = 11.sp)
                                )
                            }

                            // Card Holder Name Input
                            OutlinedTextField(
                                value = cardHolderName,
                                onValueChange = { cardHolderName = it },
                                label = { Text("Cardholder Full Name", fontSize = 11.sp) },
                                placeholder = { Text("Principal Seymour Skinner", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                        } else {
                            // UPI Virtual Address Setup Form
                            OutlinedTextField(
                                value = upiId,
                                onValueChange = { upiInputString -> 
                                    upiId = upiInputString.filter { charValue -> !charValue.isWhitespace() } 
                                },
                                label = { Text("Virtual Payment Address (VPA)", fontSize = 11.sp) },
                                placeholder = { Text("skinner@okaxis", fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                supportingText = {
                                    Text(
                                        text = if (upiId.contains("@") || upiId.isBlank()) "Supports BHIM UPI, GPay, Paytm, PhonePe" else "Missing '@' identifier domain",
                                        fontSize = 9.sp,
                                        color = if (upiId.contains("@") || upiId.isBlank()) textSecondary else Color(0xFFF43F5E)
                                    )
                                },
                                textStyle = TextStyle(fontSize = 11.sp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                if (isPaymentSuccessState) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                        onClick = {
                            showPaymentDialog = false
                            isPaymentSuccessState = false
                        }
                    ) {
                        Text("Finish Checkout", fontSize = 11.sp, color = Color.White)
                    }
                } else if (!isPaymentProcessing) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                        enabled = if (paymentMethod == "CARD") {
                            cardNumber.length >= 15 && cardExpiry.length >= 5 && cardCvv.length >= 3 && cardHolderName.isNotBlank()
                        } else {
                            upiId.contains("@") && upiId.length > 5
                        },
                        onClick = {
                            isPaymentProcessing = true
                            
                            // Sequential simulation phases using standard CoroutineScope
                            coroutineScope.launch {
                                paymentProcessingStep = "Establishing encrypted stripe handshake..."
                                kotlinx.coroutines.delay(800)
                                paymentProcessingStep = "Verifying double-factor credentials & security tokens..."
                                kotlinx.coroutines.delay(800)
                                paymentProcessingStep = "Awaiting institutional banking node authorization..."
                                kotlinx.coroutines.delay(800)
                                
                                isPaymentProcessing = false
                                isPaymentSuccessState = true
                                
                                // Live update the business database object!
                                currentPaidUntilDate = "July 30, 2026"
                                currentSubscriptionStatus = "Active"
                                
                                val updatedOrg = defaultOrg.copy(
                                    subscriptionPlan = selectedPlanName,
                                    monthlyAmount = basePlanAmount,
                                    subscriptionStartDate = "May 30, 2026",
                                    subscriptionEndDate = "July 30, 2026",
                                    status = "Active"
                                )
                                viewModel.updateOrganizationDetails(updatedOrg)
                                
                                val refId = "TXN-${(10000..99999).random()}-" + if (paymentMethod == "CARD") "CR" else "UP"
                                // prepend standard live log
                                billingLogs.add(0, Triple("June 2026", selectedPlanName, "Paid & Settled - Ref: $refId"))
                                
                                successToastMessage = "Monthly License Renewed successfully for $selectedPlanName!"
                            }
                        }
                    ) {
                        Text("Authorize ₹${grandTotalAmount.toInt()}.00", fontSize = 11.sp, color = Color.White)
                    }
                }
            },
            dismissButton = {
                if (!isPaymentProcessing) {
                    TextButton(onClick = { showPaymentDialog = false }) {
                        Text("Cancel", fontSize = 11.sp, color = textSecondary)
                    }
                }
            },
            containerColor = cardBg
        )
    }

    // 2. EDIT CONFIGURATION DIALOG
    showEditOrgDialog?.let { org ->
        AlertDialog(
            onDismissRequest = { showEditOrgDialog = null },
            title = { Text("Configure SaaS Enterprise Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editOrgName,
                        onValueChange = { editOrgName = it },
                        label = { Text("Institution Name", fontSize = 10.sp) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                    OutlinedTextField(
                        value = editOrgContact,
                        onValueChange = { editOrgContact = it },
                        label = { Text("Primary Admin Representative", fontSize = 10.sp) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                    OutlinedTextField(
                        value = editOrgMobile,
                        onValueChange = { editOrgMobile = it },
                        label = { Text("Representative Phone", fontSize = 10.sp) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                    OutlinedTextField(
                        value = editOrgEmail,
                        onValueChange = { editOrgEmail = it },
                        label = { Text("Billing Notification Email Address", fontSize = 10.sp) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                    onClick = {
                        viewModel.updateOrganizationDetails(
                            org.copy(
                                organizationName = editOrgName,
                                contactPerson = editOrgContact,
                                mobile = editOrgMobile,
                                email = editOrgEmail,
                                activeStudentCount = currentStudentsCount,
                                monthlyAmount = basePlanAmount
                            )
                        )
                        showEditOrgDialog = null
                    }
                ) {
                    Text("Apply Settings", fontSize = 11.sp, color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditOrgDialog = null }) {
                    Text("Dismiss", fontSize = 11.sp, color = textSecondary)
                }
            },
            containerColor = cardBg
        )
    }
}

@Composable
fun AdminAnalyticsTab(
    students: List<StudentProfile>,
    attendance: List<AttendanceRecord>,
    wellness: List<WellnessEntry>,
    isDark: Boolean
) {
    val textPrimary = if (isDark) Color(0xFFFFF5F0) else Color(0xFF2E190A)
    val textSecondary = if (isDark) Color(0xFFFFB088) else Color(0xFF8C3E00)
    val cardBg = if (isDark) Color(0xFF1A1009) else Color(0xFFFFF3EC)
    val cardBorder = if (isDark) Color(0xFFFF7A00).copy(0.7f) else Color(0xFFFF9E7D)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Central Analytics Reporting", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)

        // Summary counters card
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.5.dp, cardBorder),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Profiles", fontSize = 10.sp, color = textSecondary)
                    Text("${students.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Attendance Records", fontSize = 10.sp, color = textSecondary)
                    Text("${attendance.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Wellness Diagnosed", fontSize = 10.sp, color = textSecondary)
                    Text("${wellness.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                }
            }
        }

        // Custom drawn circular pie or bar layout metrics using Canvas
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.5.dp, cardBorder),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 Attendance Compliance Ratios", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(16.dp))

                val presCount = attendance.count { it.status == "Present" }
                val lateCount = attendance.count { it.status == "Late" }
                val abCount = attendance.count { it.status == "Absent" }
                val totCount = attendance.size.coerceAtLeast(1)

                val presRatio = presCount.toFloat() / totCount
                val lateRatio = lateCount.toFloat() / totCount
                val abRatio = abCount.toFloat() / totCount

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Custom Draw Canvas Bar Chart
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeW = 10.dp.toPx()
                            // Present arc
                            drawArc(
                                color = Color(0xFF10B981),
                                startAngle = 0f,
                                sweepAngle = presRatio * 360f,
                                useCenter = false,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                            // Late arc
                            drawArc(
                                color = Color(0xFFF59E0B),
                                startAngle = presRatio * 360f,
                                sweepAngle = lateRatio * 360f,
                                useCenter = false,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                            // Absent arc
                            drawArc(
                                color = Color(0xFFEF4444),
                                startAngle = (presRatio + lateRatio) * 360f,
                                sweepAngle = (abRatio * 360f).coerceAtLeast(10f), // Min degree for visibility
                                useCenter = false,
                                style = Stroke(width = strokeW, cap = StrokeCap.Round)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        LegendRow(color = Color(0xFF10B981), label = "Present (${(presRatio * 100).toInt()}%)", textColor = textPrimary)
                        LegendRow(color = Color(0xFFF59E0B), label = "Late (${(lateRatio * 100).toInt()}%)", textColor = textPrimary)
                        LegendRow(color = Color(0xFFEF4444), label = "Absent (${(abRatio * 100).toInt()}%)", textColor = textPrimary)
                    }
                }
            }
        }

        // Custom drawn sleep hours average graph
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            border = BorderStroke(1.5.dp, cardBorder),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📈 Sleep Hours Tracking (Avg vs Suggested)", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))

                val avgSleep = if (wellness.isNotEmpty()) wellness.map { it.sleepHours }.average() else 7.5
                Text(
                    text = "Weekly average: ${String.format(Locale.US, "%.1f", avgSleep)} Hours / Recommended: 8.0 Hours",
                    fontSize = 11.sp,
                    color = textSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Render dynamic graph utilizing Canvas drawing
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                ) {
                    val width = size.width
                    val height = size.height

                    // Draw reference line for suggested 8.0 hrs
                    val recommendedY = height * 0.25f
                    drawLine(
                        color = Color(0xFFEF4444).copy(0.4f),
                        start = Offset(0f, recommendedY),
                        end = Offset(width, recommendedY),
                        strokeWidth = 2.dp.toPx()
                    )

                    // Draw historical sleep curve (mock connected lines)
                    val points = listOf(
                        Offset(width * 0.1f, height * 0.6f),
                        Offset(width * 0.3f, height * 0.7f),
                        Offset(width * 0.5f, height * 0.4f),
                        Offset(width * 0.7f, height * 0.5f),
                        Offset(width * 0.9f, height * 0.3f)
                    )
                    for (i in 0 until points.size - 1) {
                        drawLine(
                            color = Color(0xFFFF7A00),
                            start = points[i],
                            end = points[i+1],
                            strokeWidth = 3.dp.toPx()
                        )
                        drawCircle(
                            color = Color(0xFFFF7A00),
                            center = points[i],
                            radius = 4.dp.toPx()
                        )
                    }
                    drawCircle(
                        color = Color(0xFFFF7A00),
                        center = points.last(),
                        radius = 4.dp.toPx()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mon", fontSize = 9.sp, color = textSecondary)
                    Text("Tue", fontSize = 9.sp, color = textSecondary)
                    Text("Wed", fontSize = 9.sp, color = textSecondary)
                    Text("Thu", fontSize = 9.sp, color = textSecondary)
                    Text("Fri (Today)", fontSize = 9.sp, color = textSecondary)
                }
            }
        }
    }
}

@Composable
fun LegendRow(color: Color, label: String, textColor: Color = Color.White) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = textColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ==========================================
// PROGRAMMATIC ARTWORK / MINI ILLUSTRATIONS
// ==========================================
@Composable
fun WellnestLogoIllustration(modifier: Modifier = Modifier, isDark: Boolean) {
    val mainIndigo = Color(0xFF4F70FA)
    val softCyan = Color(0xFF22D3EE)
    val amberGold = Color(0xFFF97316)
    val circleBg = if (isDark) Color(0xFF182246) else Color(0xFFEBF2FA)

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(circleBg)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw abstract beautiful waves / orbits representing a shield and physical wellness
            val brush1 = Brush.linearGradient(listOf(mainIndigo, softCyan))
            val brush2 = Brush.linearGradient(listOf(softCyan, amberGold))
            
            // Background blur concentric rings
            drawCircle(
                brush = brush1,
                radius = size.minDimension / 2.5f,
                center = center.copy(x = center.x - 10f),
                alpha = 0.4f
            )
            drawCircle(
                brush = brush2,
                radius = size.minDimension / 3f,
                center = center.copy(x = center.x + 15f),
                alpha = 0.5f
            )
            
            // Main glowing central nucleus
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 6f,
                center = center,
                alpha = 0.9f
            )

            // Dynamic diagonal band representing attendance/wellness analytics trends
            val path = androidx.compose.ui.graphics.Path().apply {
                moveTo(0f, size.height * 0.7f)
                quadraticTo(size.width * 0.4f, size.height * 0.2f, size.width, size.height * 0.4f)
                lineTo(size.width, size.height * 0.5f)
                quadraticTo(size.width * 0.4f, size.height * 0.3f, 0f, size.height * 0.8f)
                close()
            }
            drawPath(path = path, brush = brush2)
        }
    }
}

@Composable
fun WellnessZenIllustration(modifier: Modifier = Modifier) {
    val activeColor = Color(0xFF4F70FA) // Warm Royal Blue
    val violetColor = Color(0xFF22D3EE) // Soft Cyan
    val peachColor = Color(0xFFF97316) // Amber Gold

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val centerPoint = center

            // 1. Draw a golden horizon curve
            drawLine(
                brush = Brush.linearGradient(listOf(violetColor.copy(0.3f), peachColor.copy(0.3f))),
                start = Offset(0f, height * 0.8f),
                end = Offset(width, height * 0.8f),
                strokeWidth = 3f
            )

            // 2. Meditating core stack (represented as organic dynamic overlapping zen pebbles)
            drawCircle(
                brush = Brush.linearGradient(listOf(violetColor, activeColor)),
                radius = 18.dp.toPx(),
                center = Offset(centerPoint.x, height * 0.65f)
            )
            drawCircle(
                brush = Brush.linearGradient(listOf(activeColor, peachColor)),
                radius = 13.dp.toPx(),
                center = Offset(centerPoint.x, height * 0.46f)
            )
            drawCircle(
                brush = Brush.linearGradient(listOf(peachColor, Color.White)),
                radius = 8.dp.toPx(),
                center = Offset(centerPoint.x, height * 0.31f)
            )

            // 3. Glowing aura ring around the zen stack
            drawArc(
                brush = Brush.linearGradient(listOf(violetColor.copy(0.1f), activeColor, peachColor.copy(0.1f))),
                startAngle = -210f,
                sweepAngle = 240f,
                useCenter = false,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round),
                size = Size(70.dp.toPx(), 70.dp.toPx()),
                topLeft = Offset(centerPoint.x - 35.dp.toPx(), height * 0.28f)
            )

            // 4. Little floating energy spark dots left and right
            drawCircle(color = peachColor, radius = 3.dp.toPx(), center = Offset(centerPoint.x - 45.dp.toPx(), height * 0.45f))
            drawCircle(color = activeColor, radius = 2.dp.toPx(), center = Offset(centerPoint.x + 50.dp.toPx(), height * 0.38f))
            drawCircle(color = violetColor, radius = 3.dp.toPx(), center = Offset(centerPoint.x + 38.dp.toPx(), height * 0.6f))
        }
    }
}

@Composable
fun FinAndSaaSIllustration(modifier: Modifier = Modifier) {
    val mainIndigo = Color(0xFF4F70FA)
    val softCyan = Color(0xFF22D3EE)
    val amberGold = Color(0xFFF97316)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w / 2

            // Background abstract glowing radial gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(mainIndigo.copy(0.2f), Color.Transparent),
                    center = Offset(cx, h * 0.5f),
                    radius = w * 0.3f
                )
            )

            // Programmatic Ledger/Shield composition representing finance billing
            val rectWidth = 40.dp.toPx()
            val rectHeight = 50.dp.toPx()
            val topLeftOffset = Offset(cx - rectWidth / 2, h * 0.2f)

            // Base receipt page (slight tilt)
            drawRoundRect(
                brush = Brush.linearGradient(listOf(mainIndigo.copy(0.7f), softCyan.copy(0.7f))),
                topLeft = topLeftOffset,
                size = Size(rectWidth, rectHeight),
                cornerRadius = androidx.compose.foundation.shape.CornerSize(6.dp.toPx()).let { androidx.compose.ui.geometry.CornerRadius(8f) },
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Invoice lines programmatically
            drawLine(
                color = mainIndigo.copy(0.8f),
                start = Offset(cx - rectWidth / 3, h * 0.35f),
                end = Offset(cx + rectWidth / 3, h * 0.35f),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = amberGold.copy(0.8f),
                start = Offset(cx - rectWidth / 3, h * 0.45f),
                end = Offset(cx + rectWidth / 4, h * 0.45f),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = softCyan.copy(0.8f),
                start = Offset(cx - rectWidth / 3, h * 0.55f),
                end = Offset(cx + rectWidth / 6, h * 0.55f),
                strokeWidth = 2.dp.toPx()
            )

            // Intersecting dynamic crescent ring representing continuous subscriptions
            drawArc(
                brush = Brush.linearGradient(listOf(amberGold, mainIndigo)),
                startAngle = -20f,
                sweepAngle = 220f,
                useCenter = false,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                size = Size(50.dp.toPx(), 22.dp.toPx()),
                topLeft = Offset(cx - 25.dp.toPx(), h * 0.58f)
            )

            // Floating coin/star
            drawCircle(color = amberGold, radius = 4.dp.toPx(), center = Offset(cx + 32.dp.toPx(), h * 0.25f))
            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = Offset(cx - 30.dp.toPx(), h * 0.5f))
        }
    }
}

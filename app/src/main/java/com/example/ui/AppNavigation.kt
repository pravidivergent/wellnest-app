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
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.TextStyle

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
    val bgColorStart = if (isDark) Color(0xFF0E1428) else Color(0xFFE1EBF7)
    val bgColorEnd = if (isDark) Color(0xFF0A0F21) else Color(0xFFF3F7FC)

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
    var selectedRole by remember { mutableStateOf("STUDENT") } // STUDENT, COACH, ADMIN
    var mobileNumber by remember { mutableStateOf("") }
    var registerNumber by remember { mutableStateOf("2026CS501") } // Preloaded registration for student testing
    var hasError by remember { mutableStateOf(false) }
    val isDark by viewModel.isDarkMode.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Box(modifier = Modifier.fillMaxSize()) {
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

            // Branding Visual - Modern Programmatic Illustration
            WellnestLogoIllustration(
                modifier = Modifier,
                isDark = isDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Wellnest",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isDark) Color.White else Color(0xFF180A22),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Secure Student Daily Wellness & Attendance Analytics",
                fontSize = 13.sp,
                color = if (isDark) Color(0xFFFAF9FF).copy(0.7f) else Color(0xFF331B47),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Card Container for login contents
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
                    Text(
                        text = "SELECT PORTAL ROLE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.2.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                // Selectable Role Buttons Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0xFF0A0F21) else Color(0xFFEBF1FA)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val rolesList = listOf("STUDENT" to Icons.Default.School, "COACH" to Icons.Default.Sports, "ADMIN" to Icons.Default.AdminPanelSettings)
                    rolesList.forEach { (roleCode, icon) ->
                        val isSel = selectedRole == roleCode
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedRole = roleCode }
                                .background(if (isSel) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = roleCode,
                                    tint = if (isSel) Color.White else (if (isDark) Color(0xFF64748B) else Color(0xFF4F70FA).copy(0.7f)),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = roleCode,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) Color.White else (if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f))
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Enter details form
                Text(
                    text = "ENTER REGISTERED MOBILE",
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

                // Additional input for STUDENT role (Registration Number)
                if (selectedRole == "STUDENT") {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "STUDENT REGISTER NUMBER",
                        fontSize = 11.sp,
                        color = if (isDark) Color(0xFF94A3B8) else Color(0xFF1E293B).copy(0.7f),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = registerNumber,
                        onValueChange = { registerNumber = it },
                        placeholder = { Text("e.g. 2026CS501", color = if (isDark) Color(0xFF64748B) else Color(0xFF94A3B8)) },
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
                            Icon(Icons.Default.Badge, contentDescription = "", tint = MaterialTheme.colorScheme.primary.copy(0.7f))
                        }
                    )
                }

                if (hasError) {
                    Text(
                        text = "Please enter a valid mobile number.",
                        color = Color(0xFFEF4444),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (mobileNumber.trim().length >= 10) {
                            keyboardController?.hide()
                            viewModel.requestOtp(mobileNumber, selectedRole, registerNumber)
                        } else {
                            hasError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Secure OTP Request", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Information Callout for quick logging testing
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "💡 Demo Access Accounts:",
                    fontSize = 12.sp,
                    color = Color(0xFF38BDF8),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• Student: 2026CS501 (Alex), 2026CS502 (Siddharth)\n" +
                           "• Coach: Enter coach mobile (e.g. 9900990099)\n" +
                           "• Admin: Enter admin mobile\n" +
                           "• Code: Any 6 characters (e.g., 123456)",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(top = 4.dp),
                    lineHeight = 16.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }

    // Floating theme toggle on top-right of login screen
    IconButton(
        onClick = { viewModel.toggleDarkMode() },
        modifier = Modifier
            .statusBarsPadding()
            .align(Alignment.TopEnd)
            .padding(16.dp)
            .background(if (isDark) Color(0xFF1E293B) else Color.White, CircleShape)
            .size(44.dp)
    ) {
        Icon(
            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = "Toggle Dark/Light Mode",
            tint = Color(0xFF1ABC9C)
        )
    }
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
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.toggleDarkMode() }) {
                            Icon(
                                imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = "Toggle Dark/Light Mode",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Log out",
                                tint = Color(0xFFEF4444)
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
                    onSubmit = { sleep, b, l, d, water, energy, mood, n, i ->
                        viewModel.submitWellness(state.registerNumber, sleep, b, l, d, water, energy, mood, n, i)
                    }
                )
                "LEAVE" -> StudentLeavesTab(
                    registerNumber = state.registerNumber,
                    studentName = studentProfile?.name ?: state.name,
                    leavesList = studentLeaves,
                    onApply = { start, end, reason, proof ->
                        viewModel.applyForLeave(state.registerNumber, studentProfile?.name ?: state.name, start, end, reason, proof)
                    }
                )
                "PROF" -> StudentProfileTab(
                    studentProfile = studentProfile,
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

        // Mini description card with custom programmatic image artwork
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDark) Color(0xFF16112C) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WellnessZenIllustration()
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "YOUR MINDFUL COMPASS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFF43F5E),
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "Cultivate consistency. Combining daily mood self-reports with attendance metrics builds an integrated reflection of your campus journey.",
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        color = if (isDark) Color(0xFFFAF9FF).copy(alpha = 0.8f) else Color(0xFF331B47).copy(alpha = 0.8f),
                        modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }
        }

        // Attendance Stats Gauge Panel
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                color = if (isDark) Color(0xFF233060) else Color(0xFFE2ECF5),
                                startAngle = -220f,
                                sweepAngle = 260f,
                                useCenter = false,
                                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = if (attendancePct >= 75) Color(0xFF10B981) else Color(0xFFF59E0B),
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

        // Daily Check-ins (Morning / Evening Attendance)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚀 DIRECT SEED ATTENDANCE - TODAY",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Morning Check-in Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (morningLogged) (if (isDark) Color(0xFF0E1428) else Color(0xFFE2EDFD)) else (MaterialTheme.colorScheme.primary.copy(0.15f)))
                                .clickable(enabled = !morningLogged) {
                                    viewModel.studentCheckIn(regNo, "Morning", "Present")
                                }
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (morningLogged) Icons.Default.CheckCircle else Icons.Default.WbSunny,
                                contentDescription = "",
                                tint = if (morningLogged) Color(0xFF10B981) else Color(0xFFF97316),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Morning", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = if (morningLogged) "Checked-In" else "Check In",
                                fontSize = 10.sp,
                                color = if (morningLogged) (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)) else MaterialTheme.colorScheme.primary
                            )
                        }

                        // Evening Check-in Card
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (eveningLogged) (if (isDark) Color(0xFF0E1428) else Color(0xFFE2EDFD)) else (MaterialTheme.colorScheme.primary.copy(0.15f)))
                                .clickable(enabled = !eveningLogged) {
                                    viewModel.studentCheckIn(regNo, "Evening", "Present")
                                }
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = if (eveningLogged) Icons.Default.CheckCircle else Icons.Default.NightsStay,
                                contentDescription = "",
                                tint = if (eveningLogged) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Evening", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Text(
                                text = if (eveningLogged) "Checked-In" else "Check In",
                                fontSize = 10.sp,
                                color = if (eveningLogged) (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)) else MaterialTheme.colorScheme.primary
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                            color = Color(0xFF10B981)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF10B981).copy(0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Mood: ${lastWellnessEntry?.mood ?: "Not Logged"}",
                                fontSize = 10.sp,
                                color = Color(0xFF10B981),
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
                                Icon(Icons.Default.LocalDrink, contentDescription = "", tint = Color(0xFF22D3EE))
                                Text("Water", fontSize = 11.sp, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                                Text("${lastWellnessEntry.waterIntakeCups} cups", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                                Icon(Icons.Default.FlashOn, contentDescription = "", tint = MaterialTheme.colorScheme.tertiary)
                                Text("Energy", fontSize = 11.sp, color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                                Text("${lastWellnessEntry.energyLevel}/10", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Your Attendance Logs", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Detailed record of daily check-ins for register number: $regNo", fontSize = 11.sp, color = Color(0xFF94A3B8))
        Spacer(modifier = Modifier.height(16.dp))

        if (studentLogs.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Event, contentDescription = "", tint = Color(0xFF475569), modifier = Modifier.size(54.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No logs recorded yet.", color = Color(0xFF64748B), fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(studentLogs) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
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
                                Text(text = item.date, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                Text(text = "Shift: ${item.shift}", fontSize = 11.sp, color = Color(0xFF38BDF8))
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Daily Wellness Tracker", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Record sleep hours, meals count, energy level, and feelings to generate a healthy diagnostic report.", fontSize = 11.sp, color = Color(0xFF10B981))
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Sleep Hours Slider
                Text(
                    text = "🛌 Sleep Hours Count: ${String.format(Locale.US, "%.1f", sleepHours)} hrs",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Slider(
                    value = sleepHours,
                    onValueChange = { sleepHours = it },
                    valueRange = 1f..12f,
                    steps = 22,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF3B82F6),
                        activeTrackColor = Color(0xFF3B82F6),
                        inactiveTrackColor = Color(0xFF475569)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Meals Checklist
                Text("🍽️ Did you have meals today?", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF10B981))
                            )
                            Text(name, color = Color.White, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Water intake counter
                Text("💧 Water Intake (cups): $waterCups", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    IconButton(
                        onClick = { if (waterCups > 0) waterCups-- },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF334155), CircleShape)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Minus", tint = Color.White)
                    }
                    Text("$waterCups Cups", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    IconButton(
                        onClick = { waterCups++ },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFF1E3A8A), CircleShape)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Plus", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Energy level slider
                Text(
                    text = "⚡ Energy Level Indicator: $energy/10",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Slider(
                    value = energy.toFloat(),
                    onValueChange = { energy = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFFF59E0B),
                        activeTrackColor = Color(0xFFF59E0B),
                        inactiveTrackColor = Color(0xFF475569)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mood selector
                Text("🎭 Overall Mental Vibe / Mood:", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                SingleRowChipSelection(
                    items = moodChips,
                    selectedItem = selectedMood,
                    onSelect = { selectedMood = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Notes Field
                Text("📝 Mental Wellness Notes:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("What made you feel like this today? Describe challenges.", color = Color(0xFF64748B), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF10B981), unfocusedBorderColor = Color(0xFF475569),
                        focusedContainerColor = Color(0xFF0F172A), unfocusedContainerColor = Color(0xFF0F172A)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Improvements field
                Text("🔮 Health & Wellness Goals/Improvements:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = improvements,
                    onValueChange = { improvements = it },
                    placeholder = { Text("What goals do you have for tomorrow?", color = Color(0xFF64748B), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF10B981), unfocusedBorderColor = Color(0xFF475569),
                        focusedContainerColor = Color(0xFF0F172A), unfocusedContainerColor = Color(0xFF0F172A)
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (showSuccessToast) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981).copy(0.15f))
                            .padding(12.dp)
                    ) {
                        Text("🎉 Daily wellness diagnostics saved successfully!", color = Color(0xFF10B981), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Button(
                    onClick = {
                        onSubmit(sleepHours, brekkie, lunch, dinner, waterCups, energy, selectedMood, notes, improvements)
                        showSuccessToast = true
                        // Reset forms after timeout
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                ) {
                    Text("Securely Log Wellness", fontWeight = FontWeight.Bold)
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
                    .background(if (isSel) Color(0xFF10B981) else Color(0xFF334155))
                    .clickable { onSelect(code) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = code,
                        color = Color.White,
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
    onApply: (String, String, String, String) -> Unit
) {
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var proofName by remember { mutableStateOf("") }
    var wasSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Leave Management", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Request formal absences. Upload verification proofs for Coach reviews.", fontSize = 11.sp, color = Color(0xFF38BDF8))
        Spacer(modifier = Modifier.height(16.dp))

        // Create form
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📄 APPLICANT: $studentName", fontSize = 12.sp, color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Start Date (yyyy-MM-dd)", color = Color.White, fontSize = 11.sp)
                        OutlinedTextField(
                            value = startDate,
                            onValueChange = { startDate = it },
                            placeholder = { Text("2026-06-01", color = Color(0xFF64748B), fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF38BDF8), unfocusedBorderColor = Color(0xFF475569)
                            )
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("End Date (yyyy-MM-dd)", color = Color.White, fontSize = 11.sp)
                        OutlinedTextField(
                            value = endDate,
                            onValueChange = { endDate = it },
                            placeholder = { Text("2026-06-03", color = Color(0xFF64748B), fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF38BDF8), unfocusedBorderColor = Color(0xFF475569)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Absence Reason", color = Color.White, fontSize = 11.sp)
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    placeholder = { Text("Details or medical surgery reasons", color = Color(0xFF64748B), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF38BDF8), unfocusedBorderColor = Color(0xFF475569)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Verification Proof attachment (.jpg/.pdf)", color = Color.White, fontSize = 11.sp)
                OutlinedTextField(
                    value = proofName,
                    onValueChange = { proofName = it },
                    placeholder = { Text("e.g. prescription_receipt.pdf", color = Color(0xFF64748B), fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.AttachFile, contentDescription = "") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF38BDF8), unfocusedBorderColor = Color(0xFF475569)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (wasSubmitted) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF38BDF8).copy(0.15f))
                            .padding(12.dp)
                    ) {
                        Text("Submitted to your assigned Coach dashboard. Monitor decision report below.", color = Color(0xFF38BDF8), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8))
                ) {
                    Text("Submit Absence Request", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // History listing
        Text("Your Registered Absence History", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))

        if (leavesList.isEmpty()) {
            Text("No requested leaves found.", color = Color(0xFF64748B), fontSize = 12.sp)
        } else {
            leavesList.forEach { leave ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${leave.startDate} to ${leave.endDate}", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                            val (bg, tCol) = when (leave.status.uppercase(Locale.US)) {
                                "PENDING" -> Color(0xFFF59E0B).copy(0.15f) to Color(0xFFF59E0B)
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

                        Text(text = "Reason: ${leave.reason}", fontSize = 12.sp, color = Color(0xFF94A3B8), modifier = Modifier.padding(top = 4.dp))
                        Text(text = "File Proof: ${leave.proofName}", fontSize = 10.sp, color = Color(0xFF64748B))

                        if (leave.remarks.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF0F172A))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Text("Coach Comment:", fontSize = 10.sp, color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
                                    Text(leave.remarks, fontSize = 11.sp, color = Color.White)
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
            Text("Student Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            IconButton(onClick = { isEditState = !isEditState }) {
                Icon(
                    imageVector = if (isEditState) Icons.Default.Close else Icons.Default.Edit,
                    contentDescription = "",
                    tint = Color(0xFF38BDF8)
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
                    .background(Color(0xFF3B82F6)),
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
            Text(studentProfile.registerNumber, fontSize = 14.sp, color = Color(0xFF38BDF8), fontWeight = FontWeight.Bold)
        }

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Name
                ProfileFieldItem(label = "Student Name", value = name, enabled = isEditState, onValueChange = { name = it })
                // Course
                ProfileFieldItem(label = "Course Program", value = course, enabled = isEditState, onValueChange = { course = it })
                // Batch
                ProfileFieldItem(label = "Assigned Batch", value = batch, enabled = isEditState, onValueChange = { batch = it })
                // Mobile
                ProfileFieldItem(label = "Primary Mobile", value = mobile, enabled = isEditState, onValueChange = { mobile = it })
                // Parent Mobile
                ProfileFieldItem(label = "Parent/Guardian Mobile", value = parentMobile, enabled = isEditState, onValueChange = { parentMobile = it })
                // Address
                ProfileFieldItem(label = "Physical Address", value = address, enabled = isEditState, onValueChange = { address = it })

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
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981))
                    ) {
                        Text("Save & Sync Profile Changes", fontWeight = FontWeight.Bold)
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
    onValueChange: (String) -> Unit
) {
    Column {
        Text(label, color = Color(0xFF38BDF8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        if (enabled) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF3B82F6), unfocusedBorderColor = Color(0xFF475569),
                    focusedContainerColor = Color(0xFF0F172A), unfocusedContainerColor = Color(0xFF0F172A)
                )
            )
        } else {
            Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 2.dp))
            Divider(color = Color(0xFF334155), modifier = Modifier.padding(top = 6.dp))
        }
    }
}


// ==========================================================
// 4. COACH PORTAL LAYOUT
// ==========================================================
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

    // Counts
    val pendingLeavesCount = allLeaves.count { it.status == "Pending" }
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val absentCount = allAttendance.filter { it.date == todayStr && it.status == "Absent" }.map { it.registerNumber }.distinct().size

    // Wellness alerts count (Students with sleep hours < 5.0 or Low Energy < 4)
    val criticalAlerts = allWellness.filter { it.sleepHours <= 5f || it.energyLevel <= 4 }

    val isDark by viewModel.isDarkMode.collectAsState()
    val textPrimary = if (isDark) Color.White else Color(0xFF0F172A)
    val textSecondary = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
    val cardBg = if (isDark) Color(0xFF1E293B) else Color.White
    val topBarBg = if (isDark) Color(0xFF1E293B) else Color.White
    val accentColor = Color(0xFF1ABC9C)

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
                        IconButton(onClick = { viewModel.logout() }) {
                            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit", tint = Color(0xFFEF4444))
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("LEAVES PENDING", fontSize = 10.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                        Text("$pendingLeavesCount Requests", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("WELLNESS WARNINGS", fontSize = 10.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                        Text("${criticalAlerts.size} Critical Alerts", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEF4444))
                    }
                }
            }

            // Student Absence report tracker
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF162030)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🚨 DAILY ABSENT STUDENT LOGS ($todayStr)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF87171)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val todayAbsences = allAttendance.filter { it.date == todayStr && it.status == "Absent" }
                    if (todayAbsences.isEmpty()) {
                        Text("Excellent! No students marked absent in morning/evening shifts today.", fontSize = 11.sp, color = Color(0xFF94A3B8))
                    } else {
                        todayAbsences.forEach { ab ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val sName = students.find { it.registerNumber == ab.registerNumber }?.name ?: ab.registerNumber
                                Text(sName, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Shift: ${ab.shift}", color = Color(0xFFF87171), fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // Leave Approvals List
            Text("Pending Absence / Leave Requests", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)

            val pendingLeaves = allLeaves.filter { it.status == "Pending" }
            if (pendingLeaves.isEmpty()) {
                Text("All leave applications reviewed! Good job.", color = Color(0xFF64748B), fontSize = 12.sp)
            } else {
                pendingLeaves.forEach { leave ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(leave.studentName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    Text("Reg: ${leave.studentRegister}", fontSize = 11.sp, color = Color(0xFF94A3B8))
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF38BDF8).copy(0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text("${leave.startDate} to ${leave.endDate}", color = Color(0xFF38BDF8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Reason: ${leave.reason}", fontSize = 12.sp, color = Color.White)
                            Text("Proof: ${leave.proofName}", fontSize = 11.sp, color = Color(0xFF10B981))

                            Spacer(modifier = Modifier.height(12.dp))

                            // Approve/Reject Controls
                            if (selectedLeaveForRemark?.id == leave.id) {
                                OutlinedTextField(
                                    value = coachRemarks,
                                    onValueChange = { coachRemarks = it },
                                    placeholder = { Text("Add comments or remarks to student...", color = Color(0xFF64748B), fontSize = 12.sp) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
                                        focusedContainerColor = Color(0xFF0F172A), unfocusedContainerColor = Color(0xFF0F172A)
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
                                        Text("Accept Approve", fontSize = 11.sp)
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
                                        Text("Reject", fontSize = 11.sp)
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
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                                ) {
                                    Text("Decision / Remarks")
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
                        colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF291F24) else Color(0xFFFEE2E2)),
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
                                val sName = students.find { it.registerNumber == entry.registerNumber }?.name ?: entry.registerNumber
                                Text(sName, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color(0xFF991B1B), fontSize = 13.sp)
                                Text("Sleep Level: ${entry.sleepHours}h | Energy rating: ${entry.energyLevel}/10", fontSize = 11.sp, color = if (isDark) Color(0xFFFCA5A5) else Color(0xFFB91C1C))
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
            students.forEach { s ->
                val isSelected = expandedCoachStudentId == s.registerNumber
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardBg),
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
                                        Text("${latestLog.waterIntakeCups} Cups / Liters", fontSize = 11.sp, color = textPrimary)
                                    }

                                    // 4. Energy level
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Bolt, contentDescription = "Energy", tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Energy Indicator: ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                                        Text("${latestLog.energyLevel}/10 Rating Scale", fontSize = 11.sp, color = textPrimary)
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

    val textPrimary = if (isDark) Color.White else Color(0xFF0F172A)
    val textSecondary = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
    val cardBg = if (isDark) Color(0xFF1E293B) else Color.White
    val topBarBg = if (isDark) Color(0xFF1E293B) else Color.White
    val accentColor = Color(0xFF14B8A6) // Teal

    // Admin Enrollment Forms
    var registerNum by remember { mutableStateOf("") }
    var enrollmentName by remember { mutableStateOf("") }
    var addressStr by remember { mutableStateOf("") }
    var mobileNo by remember { mutableStateOf("") }
    var parentNo by remember { mutableStateOf("") }
    var batchStr by remember { mutableStateOf("") }
    var courseStr by remember { mutableStateOf("") }
    var successToast by remember { mutableStateOf("") }

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
                            IconButton(onClick = { viewModel.toggleDarkMode() }) {
                                Icon(
                                    imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                    contentDescription = "Toggle Theme",
                                    tint = accentColor
                                )
                            }
                            IconButton(onClick = { viewModel.logout() }) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Exit", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }

                TabRow(
                    selectedTabIndex = when (activeTab) {
                        "ANALYTICS" -> 0
                        "MANAGE_STUDENTS" -> 1
                        "FEES_MANAGEMENT" -> 2
                        "SUBSCRIPTION_BILLING" -> 3
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
                        selected = activeTab == "FEES_MANAGEMENT",
                        onClick = { activeTab = "FEES_MANAGEMENT" },
                        text = { Text("Fees", fontSize = 10.sp, color = textPrimary, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = activeTab == "SUBSCRIPTION_BILLING",
                        onClick = { activeTab = "SUBSCRIPTION_BILLING" },
                        text = { Text("Billing", fontSize = 10.sp, color = textPrimary, fontWeight = FontWeight.Bold) }
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
                        students = students,
                        attendance = allAttendance,
                        wellness = allWellness
                    )
                }
                "MANAGE_STUDENTS" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Add New Student Profile", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)

                        OutlinedTextField(value = registerNum, onValueChange = { registerNum = it }, placeholder = { Text("Reg Number e.g. 2026CS509", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        OutlinedTextField(value = enrollmentName, onValueChange = { enrollmentName = it }, placeholder = { Text("Full Name", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        OutlinedTextField(value = addressStr, onValueChange = { addressStr = it }, placeholder = { Text("Local Address", color = Color(0xFF64748B)) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(value = mobileNo, onValueChange = { mobileNo = it }, placeholder = { Text("Mobile Phone", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                            OutlinedTextField(value = parentNo, onValueChange = { parentNo = it }, placeholder = { Text("Parent Phone", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(value = batchStr, onValueChange = { batchStr = it }, placeholder = { Text("Batch e.g. CS-A", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                            OutlinedTextField(value = courseStr, onValueChange = { courseStr = it }, placeholder = { Text("Course Program", color = Color(0xFF64748B)) }, modifier = Modifier.weight(1f), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
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
                                        profilePhoto = "avatar_1"
                                    )
                                    viewModel.saveStudentProfile(nProfile)
                                    successToast = "Successfully enrolled student record: $registerNum"
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
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF38BDF8))
                        ) {
                            Text("Register Student", fontWeight = FontWeight.Bold)
                        }

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
                                                                    Text("💤 Sleep: ${record.sleepHours} hrs | 💧 Water: ${record.waterIntakeCups} cups", fontSize = 9.sp, color = textPrimary)
                                                                }
                                                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                                                                    Text("🍳 Meals: Breakfast: ${if (record.hadBreakfast) "Yes" else "No"} | Lunch: ${if (record.hadLunch) "Yes" else "No"} | Dinner: ${if (record.hadDinner) "Yes" else "No"}", fontSize = 9.sp, color = textPrimary)
                                                                }
                                                                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                                                                    Text("⚡ Energy: ${record.energyLevel}/10 | 🧠 Mood: ${record.mood}", fontSize = 9.sp, color = textPrimary)
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
                }
                "FEES_MANAGEMENT" -> {
                    AdminFeesManagementTab(
                        viewModel = viewModel,
                        students = students,
                        allFees = allFees,
                        isDark = isDark
                    )
                }
                "SUBSCRIPTION_BILLING" -> {
                    AdminSubscriptionBillingTab(
                        viewModel = viewModel,
                        students = students,
                        allOrganizations = allOrganizations,
                        isDark = isDark
                    )
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
    isDark: Boolean
) {
    val textPrimary = if (isDark) Color.White else Color(0xFF0F172A)
    val textSecondary = if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
    val cardBg = if (isDark) Color(0xFF1E293B) else Color.White
    val accentColor = Color(0xFF14B8A6) // Teal

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
        modifier = Modifier
            .fillMaxSize()
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
    isDark: Boolean
) {
    val textPrimary = if (isDark) Color.White else Color(0xFF180A22)
    val textSecondary = if (isDark) Color(0xFFE2E0FF).copy(0.7f) else Color(0xFF331B47).copy(0.7f)
    val cardBg = if (isDark) Color(0xFF16112C) else Color.White
    val accentColor = Color(0xFFF43F5E) // Sunset Rose

    // Form inputs for organization customization
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

    // Compute dynamically: active students count * 100rs monthly charge
    val currentStudentsCount = students.size
    val standardChargeRate = 100.0
    val dynamicMonthlyDue = currentStudentsCount * standardChargeRate

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Institution Subscription Billing", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)

        // Descriptive Card with Custom Programmatic Vector Image
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FinAndSaaSIllustration()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "ENTERPRISE PORTAL LEDGER",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8B5CF6),
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Manage institutional usage fees transparently. Base quotas sync automatically with registered active student enrollment.",
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    color = textSecondary,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
                )
            }
        }

        // Subscription Summary Cards
        Card(
            colors = CardDefaults.cardColors(containerColor = cardBg),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(defaultOrg.organizationName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text("Contact: ${defaultOrg.contactPerson} | ${defaultOrg.email}", fontSize = 11.sp, color = textSecondary)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF22C55E).copy(0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(defaultOrg.status, color = Color(0xFF22C55E), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(color = textSecondary.copy(alpha = 0.1f))

                // Billing Calculations
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text("Active Student Base", fontSize = 10.sp, color = textSecondary)
                        Text("$currentStudentsCount Enrolled", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("SaaS Plan Mode", fontSize = 10.sp, color = textSecondary)
                        Text("₹100 / Stud / Mo", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Monthly Amount Due", fontSize = 10.sp, color = textSecondary)
                        Text("₹${dynamicMonthlyDue.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    }
                }
            }
        }

        // Live Calculations Card
        Card(
            colors = CardDefaults.cardColors(containerColor = if (isDark) Color(0xFF162030) else Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("📊 Monthly Calculation Ledger", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Database Enrollers", fontSize = 11.sp, color = textSecondary)
                    Text("$currentStudentsCount active students", fontSize = 11.sp, color = textPrimary, fontWeight = FontWeight.Bold)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Standard Charge Rate", fontSize = 11.sp, color = textSecondary)
                    Text("₹100 / Student", fontSize = 11.sp, color = textPrimary)
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Service Class Model", fontSize = 11.sp, color = textSecondary)
                    Text("Education Enterprise SaaS", fontSize = 11.sp, color = textPrimary)
                }

                HorizontalDivider(color = textSecondary.copy(0.1f), modifier = Modifier.padding(vertical = 4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Calculated Invoices Due", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                    Text("₹${dynamicMonthlyDue.toInt()}.00", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = accentColor)
                }
            }
        }

        // Configuration setup
        Button(
            onClick = {
                editOrgName = defaultOrg.organizationName
                editOrgContact = defaultOrg.contactPerson
                editOrgMobile = defaultOrg.mobile
                editOrgEmail = defaultOrg.email
                showEditOrgDialog = defaultOrg
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Edit Configuration", tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Edit Organization Profile Settings", fontWeight = FontWeight.Bold, color = Color.White)
        }

        // Historical Ledger Cycles
        Text("📅 Historical Billing Ledger Cycles", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)

        listOf(
            Triple("May 2026", "₹${dynamicMonthlyDue.toInt()}", "Generated - Awaiting Auto-Renewal"),
            Triple("April 2026", "₹${dynamicMonthlyDue.toInt()}", "Paid & Settled - Ref ID: BIL7982"),
            Triple("March 2026", "₹${dynamicMonthlyDue.toInt()}", "Paid & Settled - Ref ID: BIL3221")
        ).forEach { bRecord ->
            Card(
                colors = CardDefaults.cardColors(containerColor = cardBg),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(bRecord.first, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textPrimary)
                        Text(bRecord.third, fontSize = 10.sp, color = textSecondary)
                    }
                    Text(bRecord.second, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = accentColor)
                }
            }
        }
    }

    // Edit organization dialog
    showEditOrgDialog?.let { org ->
        AlertDialog(
            onDismissRequest = { showEditOrgDialog = null },
            title = { Text("Configure SaaS Enterprise Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editOrgName,
                        onValueChange = { editOrgName = it },
                        placeholder = { Text("Institution Name", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                    OutlinedTextField(
                        value = editOrgContact,
                        onValueChange = { editOrgContact = it },
                        placeholder = { Text("Primary Admin Representative", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                    OutlinedTextField(
                        value = editOrgMobile,
                        onValueChange = { editOrgMobile = it },
                        placeholder = { Text("Representative Phone", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        textStyle = TextStyle(fontSize = 11.sp)
                    )
                    OutlinedTextField(
                        value = editOrgEmail,
                        onValueChange = { editOrgEmail = it },
                        placeholder = { Text("Billing Notification Email Address", fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
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
                                monthlyAmount = currentStudentsCount * 100.0
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
    wellness: List<WellnessEntry>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Central Analytics Reporting", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)

        // Summary counters card
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Profiles", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("${students.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Attendance Records", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("${attendance.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Wellness Diagnosed", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    Text("${wellness.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }

        // Custom drawn circular pie or bar layout metrics using Canvas
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF162030)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📊 Attendance Compliance Ratios", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
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
                        LegendRow(color = Color(0xFF10B981), label = "Present (${(presRatio * 100).toInt()}%)")
                        LegendRow(color = Color(0xFFF59E0B), label = "Late (${(lateRatio * 100).toInt()}%)")
                        LegendRow(color = Color(0xFFEF4444), label = "Absent (${(abRatio * 100).toInt()}%)")
                    }
                }
            }
        }

        // Custom drawn sleep hours average graph
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF162030)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📈 Sleep Hours Tracking (Avg vs Suggested)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(12.dp))

                val avgSleep = if (wellness.isNotEmpty()) wellness.map { it.sleepHours }.average() else 7.5
                Text(
                    text = "Weekly average: ${String.format(Locale.US, "%.1f", avgSleep)} Hours / Recommended: 8.0 Hours",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
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
                            color = Color(0xFF38BDF8),
                            start = points[i],
                            end = points[i+1],
                            strokeWidth = 3.dp.toPx()
                        )
                        drawCircle(
                            color = Color(0xFF38BDF8),
                            center = points[i],
                            radius = 4.dp.toPx()
                        )
                    }
                    drawCircle(
                        color = Color(0xFF38BDF8),
                        center = points.last(),
                        radius = 4.dp.toPx()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mon", fontSize = 9.sp, color = Color(0xFF64748B))
                    Text("Tue", fontSize = 9.sp, color = Color(0xFF64748B))
                    Text("Wed", fontSize = 9.sp, color = Color(0xFF64748B))
                    Text("Thu", fontSize = 9.sp, color = Color(0xFF64748B))
                    Text("Fri (Today)", fontSize = 9.sp, color = Color(0xFF64748B))
                }
            }
        }
    }
}

@Composable
fun LegendRow(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
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

package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val studentDao: StudentDao,
    private val attendanceDao: AttendanceDao,
    private val leaveDao: LeaveDao,
    private val wellnessDao: WellnessDao,
    private val studentFeeDao: StudentFeeDao,
    private val organizationDao: OrganizationDao,
    private val userAccountDao: UserAccountDao,
    private val coachDao: CoachDao,
    private val tournamentDao: TournamentDao,
    private val studentDocumentDao: StudentDocumentDao,
    private var firestoreSyncManager: FirestoreSyncManager? = null
) {
    // Inject sync manager dynamically
    fun setSyncManager(manager: FirestoreSyncManager) {
        this.firestoreSyncManager = manager
    }

    // Tournament Actions
    val allTournamentsFlow: Flow<List<Tournament>> = tournamentDao.getAllTournamentsFlow()

    suspend fun insertTournament(tournament: Tournament) {
        tournamentDao.insertTournament(tournament)
        val docId = if (tournament.id == 0) "${tournament.title}_${tournament.date}" else tournament.id.toString()
        firestoreSyncManager?.uploadToCloud("tournaments", docId, tournament)
    }

    suspend fun deleteTournament(tournament: Tournament) {
        tournamentDao.deleteTournament(tournament)
        val docId = if (tournament.id == 0) "${tournament.title}_${tournament.date}" else tournament.id.toString()
        firestoreSyncManager?.deleteFromCloud("tournaments", docId)
    }

    // Student Document Actions
    val allDocumentsFlow: Flow<List<StudentDocument>> = studentDocumentDao.getAllDocumentsFlow()

    fun getStudentDocumentsFlow(regNo: String): Flow<List<StudentDocument>> {
        return studentDocumentDao.getStudentDocumentsFlow(regNo)
    }

    suspend fun insertDocument(document: StudentDocument) {
        studentDocumentDao.insertDocument(document)
        val docId = if (document.id == 0) "${document.registerNumber}_${document.documentName}" else document.id.toString()
        firestoreSyncManager?.uploadToCloud("student_documents", docId, document)
    }

    suspend fun updateDocument(document: StudentDocument) {
        studentDocumentDao.updateDocument(document)
        val docId = if (document.id == 0) "${document.registerNumber}_${document.documentName}" else document.id.toString()
        firestoreSyncManager?.uploadToCloud("student_documents", docId, document)
    }

    suspend fun deleteDocument(document: StudentDocument) {
        studentDocumentDao.deleteDocument(document)
        val docId = if (document.id == 0) "${document.registerNumber}_${document.documentName}" else document.id.toString()
        firestoreSyncManager?.deleteFromCloud("student_documents", docId)
    }

    // Coach Actions
    val allCoachesFlow: Flow<List<CoachProfile>> = coachDao.getAllCoachesFlow()

    suspend fun insertCoach(coach: CoachProfile) {
        coachDao.insertCoach(coach)
        firestoreSyncManager?.uploadToCloud("coach_profiles", coach.username, coach)
    }

    suspend fun updateCoach(coach: CoachProfile) {
        coachDao.updateCoach(coach)
        firestoreSyncManager?.uploadToCloud("coach_profiles", coach.username, coach)
    }

    suspend fun deleteCoach(coach: CoachProfile) {
        coachDao.deleteCoach(coach)
        firestoreSyncManager?.deleteFromCloud("coach_profiles", coach.username)
    }

    // Student Actions
    val allStudentsFlow: Flow<List<StudentProfile>> = studentDao.getAllStudentsFlow()
    
    fun getStudentProfileFlow(regNo: String): Flow<StudentProfile?> {
        return studentDao.getStudentProfileFlow(regNo)
    }

    suspend fun getStudentProfileDirect(regNo: String): StudentProfile? {
        return studentDao.getStudentProfileDirect(regNo)
    }

    suspend fun insertStudentProfile(student: StudentProfile) {
        studentDao.insertStudentProfile(student)
        firestoreSyncManager?.uploadToCloud("student_profiles", student.registerNumber, student)
    }

    suspend fun getAllStudentsDirect(): List<StudentProfile> {
        return studentDao.getAllStudentsDirect()
    }

    // Attendance Actions
    val allAttendanceFlow: Flow<List<AttendanceRecord>> = attendanceDao.getAllAttendanceFlow()

    fun getStudentAttendanceFlow(regNo: String): Flow<List<AttendanceRecord>> {
        return attendanceDao.getStudentAttendanceFlow(regNo)
    }

    suspend fun insertAttendance(record: AttendanceRecord) {
        attendanceDao.insertAttendance(record)
        val docId = "${record.registerNumber}_${record.date}_${record.shift}"
        firestoreSyncManager?.uploadToCloud("attendance_records", docId, record)
    }

    // Leave Actions
    val allLeavesFlow: Flow<List<LeaveApplication>> = leaveDao.getAllLeavesFlow()

    fun getStudentLeavesFlow(regNo: String): Flow<List<LeaveApplication>> {
        return leaveDao.getStudentLeavesFlow(regNo)
    }

    suspend fun insertLeave(leave: LeaveApplication) {
        leaveDao.insertLeave(leave)
        val docId = "${leave.studentRegister}_${leave.startDate}"
        firestoreSyncManager?.uploadToCloud("leave_applications", docId, leave)
    }

    suspend fun updateLeave(leave: LeaveApplication) {
        leaveDao.updateLeave(leave)
        val docId = "${leave.studentRegister}_${leave.startDate}"
        firestoreSyncManager?.uploadToCloud("leave_applications", docId, leave)
    }

    // Wellness Actions
    val allWellnessEntriesFlow: Flow<List<WellnessEntry>> = wellnessDao.getAllWellnessEntriesFlow()

    fun getStudentWellnessEntriesFlow(regNo: String): Flow<List<WellnessEntry>> {
        return wellnessDao.getStudentWellnessEntriesFlow(regNo)
    }

    suspend fun insertWellnessEntry(entry: WellnessEntry) {
        wellnessDao.insertWellnessEntry(entry)
        val docId = "${entry.registerNumber}_${entry.date}"
        firestoreSyncManager?.uploadToCloud("wellness_entries", docId, entry)
    }

    // Student Fees Actions
    val allFeesFlow: Flow<List<StudentFee>> = studentFeeDao.getAllFeesFlow()

    fun getStudentFeesFlow(regNo: String): Flow<List<StudentFee>> {
        return studentFeeDao.getStudentFeesFlow(regNo)
    }

    suspend fun insertFee(fee: StudentFee) {
        studentFeeDao.insertFee(fee)
        val docId = "${fee.studentRegister}_${fee.month}_${fee.year}"
        firestoreSyncManager?.uploadToCloud("student_fees", docId, fee)
    }

    suspend fun updateFee(fee: StudentFee) {
        studentFeeDao.updateFee(fee)
        val docId = "${fee.studentRegister}_${fee.month}_${fee.year}"
        firestoreSyncManager?.uploadToCloud("student_fees", docId, fee)
    }

    suspend fun deleteFee(fee: StudentFee) {
        studentFeeDao.deleteFee(fee)
        val docId = "${fee.studentRegister}_${fee.month}_${fee.year}"
        firestoreSyncManager?.deleteFromCloud("student_fees", docId)
    }

    // Organization Actions
    val allOrganizationsFlow: Flow<List<Organization>> = organizationDao.getOrganizationsFlow()

    suspend fun insertOrganization(org: Organization) {
        organizationDao.insertOrganization(org)
        firestoreSyncManager?.uploadToCloud("organizations", org.organizationName, org)
    }

    suspend fun updateOrganization(org: Organization) {
        organizationDao.updateOrganization(org)
        firestoreSyncManager?.uploadToCloud("organizations", org.organizationName, org)
    }

    // User Account Actions
    val allAccountsFlow: Flow<List<UserAccount>> = userAccountDao.getAllAccountsFlow()

    suspend fun getAccountByPhone(phone: String): UserAccount? {
        return userAccountDao.getAccountByPhoneDirect(phone)
    }

    suspend fun insertUserAccount(account: UserAccount) {
        userAccountDao.insertAccount(account)
        firestoreSyncManager?.uploadToCloud("user_accounts", account.phoneNumber, account)
    }
}

package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val studentDao: StudentDao,
    private val attendanceDao: AttendanceDao,
    private val leaveDao: LeaveDao,
    private val wellnessDao: WellnessDao,
    private val studentFeeDao: StudentFeeDao,
    private val organizationDao: OrganizationDao
) {
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
    }

    // Leave Actions
    val allLeavesFlow: Flow<List<LeaveApplication>> = leaveDao.getAllLeavesFlow()

    fun getStudentLeavesFlow(regNo: String): Flow<List<LeaveApplication>> {
        return leaveDao.getStudentLeavesFlow(regNo)
    }

    suspend fun insertLeave(leave: LeaveApplication) {
        leaveDao.insertLeave(leave)
    }

    suspend fun updateLeave(leave: LeaveApplication) {
        leaveDao.updateLeave(leave)
    }

    // Wellness Actions
    val allWellnessEntriesFlow: Flow<List<WellnessEntry>> = wellnessDao.getAllWellnessEntriesFlow()

    fun getStudentWellnessEntriesFlow(regNo: String): Flow<List<WellnessEntry>> {
        return wellnessDao.getStudentWellnessEntriesFlow(regNo)
    }

    suspend fun insertWellnessEntry(entry: WellnessEntry) {
        wellnessDao.insertWellnessEntry(entry)
    }

    // Student Fees Actions
    val allFeesFlow: Flow<List<StudentFee>> = studentFeeDao.getAllFeesFlow()

    fun getStudentFeesFlow(regNo: String): Flow<List<StudentFee>> {
        return studentFeeDao.getStudentFeesFlow(regNo)
    }

    suspend fun insertFee(fee: StudentFee) {
        studentFeeDao.insertFee(fee)
    }

    suspend fun updateFee(fee: StudentFee) {
        studentFeeDao.updateFee(fee)
    }

    suspend fun deleteFee(fee: StudentFee) {
        studentFeeDao.deleteFee(fee)
    }

    // Organization Actions
    val allOrganizationsFlow: Flow<List<Organization>> = organizationDao.getOrganizationsFlow()

    suspend fun insertOrganization(org: Organization) {
        organizationDao.insertOrganization(org)
    }

    suspend fun updateOrganization(org: Organization) {
        organizationDao.updateOrganization(org)
    }
}

package com.desss.collegeproduct.module.studentSubModule.attendance.model

data class StudentAttendanceModel(
    val Month: String,
    val Year: String,
    val all_count: Int,
    val count: Int,
    val errmsg: String,
    var holidayCount: Int
)


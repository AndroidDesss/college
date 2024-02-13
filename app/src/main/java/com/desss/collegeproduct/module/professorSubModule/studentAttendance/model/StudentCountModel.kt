package com.desss.collegeproduct.module.professorSubModule.studentAttendance.model

data class StudentCountModel(
    val not_present_list: List<String>,
    val present_list: List<String>,
    val total_count: Int,
    val total_not_present_count: Int,
    val total_present_count: Int
)
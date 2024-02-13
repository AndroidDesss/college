package com.desss.collegeproduct.module.professorSubModule.professorAttendance.model

data class ProfessorCountModel(
    val present_list: List<String>,
    val total_count: Int,
    val total_late_count: Int,
    val total_not_present_count: Int,
    val total_present_count: Int
)
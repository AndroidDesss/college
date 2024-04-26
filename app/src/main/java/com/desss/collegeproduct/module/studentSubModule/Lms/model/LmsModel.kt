package com.desss.collegeproduct.module.studentSubModule.Lms.model

data class LmsModel(
    val created_at: String,
    val degree: String,
    val department: String,
    val id: String,
    val is_deleted: String,
    val is_status: String,
    val qus_ans: List<QusAns>,
    val semester: String,
    val title: String,
    val updated_at: String,
    val url: String
)
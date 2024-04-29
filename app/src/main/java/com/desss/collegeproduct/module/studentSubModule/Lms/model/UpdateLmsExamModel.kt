package com.desss.collegeproduct.module.studentSubModule.Lms.model

data class UpdateLmsExamModel(
    val answer_count: String,
    val created_at: String,
    val id: String,
    val is_deleted: String,
    val is_status: String,
    val lms_id: String,
    val question_count: String,
    val qus_ans: String,
    val updated_at: String,
    val user_id: String
)
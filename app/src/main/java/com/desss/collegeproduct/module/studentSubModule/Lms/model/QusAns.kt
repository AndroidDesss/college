package com.desss.collegeproduct.module.studentSubModule.Lms.model

data class QusAns(
    val correct_answer: String,
    val options: List<String>,
    val question_id: Int,
    val question_text: String
)
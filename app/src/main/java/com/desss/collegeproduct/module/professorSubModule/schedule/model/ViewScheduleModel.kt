package com.desss.collegeproduct.module.professorSubModule.schedule.model

data class ViewScheduleModel(
    val course: String,
    val created_at: String,
    val date: String,
    val degree: String,
    val from_time: String,
    val id: String,
    val is_deleted: String,
    val notes: String,
    val section: String,
    val semester: String,
    val status: String,
    val to_time: String,
    val updated_at: String,
    val user_id: String,
    val errmsg: String
)
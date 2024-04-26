package com.desss.collegeproduct.commonfunctions

import android.app.Dialog
import com.desss.collegeproduct.R

object Constants {

    const val USER_PREFERENCE = "user_pref"

    var dialog: Dialog? = null

    val syllabusDownloadUrl = "https://edusync.desss-portfolio.com/dynamic/syllabus/"

    val notesDownloadUrl = "https://edusync.desss-portfolio.com/dynamic/notes/"

    val timetableDownloadUrl = "https://edusync.desss-portfolio.com/dynamic/timetables/"

    val studentResultsDownloadUrl = "https://edusync.desss-portfolio.com/dynamic/timetables_results/"

    val studentCategoriesBackGroundColor =
        arrayOf("#8b88d0", "#79b1fb", "#fbb97c", "#df90c9", "#f7937e", "#e5a9ac","#8b88d0","#8b88d0","#79b1fb", "#fbb97c")

    val studentCategoriesArray =
        arrayOf("Fee Pay", "Attendance", "Results","Syllabus", "Exam Timetable", "Reports", "Notes","Meetings","LMS", "Transport")

    val studentCategoriesImage = arrayOf(
        R.drawable.fee_pay,
        R.drawable.student_attendance,
        R.drawable.student_results,
        R.drawable.student_syllabus,
        R.drawable.exam_timetable,
        R.drawable.student_remark,
        R.drawable.student_notes,
        R.drawable.fee_pay,
        R.drawable.student_attendance,
        R.drawable.student_results
    )

    val professorCategoriesBackGroundColor =
        arrayOf("#8b88d0", "#79b1fb", "#fbb97c", "#df90c9", "#f7937e", "#e5a9ac","#8b88d0")

    val professorCategoriesArray = arrayOf(
        "Student Attendance",
        "Professor Attendance",
        "Department",
        "Schedule",
        "Report",
        "Notes",
        "Transport"
    )

    val professorCategoriesImage = arrayOf(
        R.drawable.student_attendance,
        R.drawable.professor_attendance,
        R.drawable.department_icon,
        R.drawable.schedule_icon,
        R.drawable.student_remark,
        R.drawable.student_notes,
        R.drawable.student_attendance,
    )

    val managementCategoriesArray =
        arrayOf("Fee Details", "Student Details", "Professor Details")

    val managementCategoriesImage = arrayOf(
        R.drawable.management_fees_details_icon,
        R.drawable.management_student_details_icon,
        R.drawable.management_professor_details_icon,
    )

    val managementCategoriesBackGroundColor =
        arrayOf("#8b88d0", "#79b1fb", "#df90c9")

    val PICK_PDF_FILE_REQUEST_CODE = 1

    val RazorPayKeyId = "rzp_test_gwzWiGa1uIEbxZ"

}
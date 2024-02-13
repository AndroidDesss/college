package com.desss.collegeproduct.module.commonDashBoardFragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.AdapterCategoriesBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.home.model.CategoriesModel
import com.desss.collegeproduct.module.managementSubModule.feeDetail.FeeDetailFragmentScreen
import com.desss.collegeproduct.module.managementSubModule.professorDetail.fragment.ProfessorDetailFragmentScreen
import com.desss.collegeproduct.module.managementSubModule.studentDetail.fragment.StudentDetailFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.department.fragment.DepartmentFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.notes.fragment.ProfessorNotesFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.professorAttendance.fragment.ProfessorAttendanceFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.report.fragment.ReportFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.schedule.fragment.ScheduleFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.studentAttendance.fragment.StudentAttendanceFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.attendance.fragment.AttendanceFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.examTimeTable.fragment.ExamTimeTableFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.feePay.fragment.FeePayFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.notes.fragment.NotesFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.remarks.fragment.RemarksFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.results.fragment.StudentResultsFragmentScreen
import com.desss.collegeproduct.module.studentSubModule.syllabus.fragment.SyllabusFragmentScreen

class CategoriesAdapter(
    private val context: Context?,
    private val categoriesModelList: List<CategoriesModel>
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private var categoriesModel: CategoriesModel? = null

    private var screenName: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterCategoriesBinding: AdapterCategoriesBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_categories,
            parent,
            false
        )
        return ViewHolder(adapterCategoriesBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        categoriesModel = categoriesModelList[position]
        holder.binding.categoriesName.text = categoriesModel!!.categoriesName
        holder.binding.parentLayout.setBackgroundColor(
            android.graphics.Color.parseColor(
                categoriesModel!!.categoriesColor
            )
        )
        holder.binding.categoriesImage.setImageResource(categoriesModel!!.categoriesImg)
        holder.itemView.setOnClickListener {
            categoriesModel = categoriesModelList[position]
            screenName = categoriesModel!!.categoriesName
            when (screenName) {
                "Fee Pay" -> {
                    val feePayFragmentScreen = FeePayFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        feePayFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Attendance" -> {
                    val attendanceFragmentScreen = AttendanceFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        attendanceFragmentScreen,
                        R.id.container,
                        true
                    )

                }

                "Results" -> {
                    val studentResultsFragmentScreen = StudentResultsFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        studentResultsFragmentScreen,
                        R.id.container,
                        true
                    )

                }
                "Syllabus" -> {
                    val syllabusFragmentScreen = SyllabusFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        syllabusFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Exam Timetable" -> {
                    val examTimeTableFragmentScreen = ExamTimeTableFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        examTimeTableFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Reports" -> {
                    val remarksFragmentScreen = RemarksFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        remarksFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Notes" -> {
                    if (SharedPref.getRollId(context) == "4") //Student
                    {
                        val notesFragmentScreen = NotesFragmentScreen()
                        CommonUtility.navigateToFragment(
                            (context as FragmentActivity).supportFragmentManager,
                            notesFragmentScreen,
                            R.id.container,
                            true
                        )
                    } else if (SharedPref.getRollId(context) == "3") //Professor
                    {
                        val professorNotesFragmentScreen = ProfessorNotesFragmentScreen()
                        CommonUtility.navigateToFragment(
                            (context as FragmentActivity).supportFragmentManager,
                            professorNotesFragmentScreen,
                            R.id.container,
                            true
                        )
                    }
                }

                "Student Attendance" -> {
                    val studentAttendanceFragmentScreen = StudentAttendanceFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        studentAttendanceFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Professor Attendance" -> {
                    val professorAttendanceFragmentScreen = ProfessorAttendanceFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        professorAttendanceFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Department" -> {
                    val departmentFragmentScreen = DepartmentFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        departmentFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Schedule" -> {
                    val scheduleFragmentScreen = ScheduleFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        scheduleFragmentScreen,
                        R.id.container,
                        true
                    )
                }

                "Report" -> {
                    val reportFragmentScreen = ReportFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        reportFragmentScreen,
                        R.id.container,
                        true
                    )
                }
                "Fee Details" -> {
                    val feeDetailFragmentScreen = FeeDetailFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        feeDetailFragmentScreen,
                        R.id.container,
                        true
                    )
                }
                "Student Details" -> {
                    val studentDetailFragmentScreen = StudentDetailFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        studentDetailFragmentScreen,
                        R.id.container,
                        true
                    )
                }
                "Professor Details" -> {
                    val professorDetailFragmentScreen = ProfessorDetailFragmentScreen()
                    CommonUtility.navigateToFragment(
                        (context as FragmentActivity).supportFragmentManager,
                        professorDetailFragmentScreen,
                        R.id.container,
                        true
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return categoriesModelList.size
    }


    class ViewHolder(binding: AdapterCategoriesBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterCategoriesBinding

        init {
            this.binding = binding
        }
    }

}


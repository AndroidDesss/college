package com.desss.collegeproduct.module.professorSubModule.schedule.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.CommonValidation
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.AdapterScheduleBinding
import com.desss.collegeproduct.databinding.UpdateSchedulePopupBinding
import com.desss.collegeproduct.module.professorSubModule.schedule.fragment.ScheduleFragmentScreen
import com.desss.collegeproduct.module.professorSubModule.schedule.model.ViewScheduleModel
import com.desss.collegeproduct.module.professorSubModule.schedule.viewmodel.ScheduleFragmentScreenViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar
import kotlin.random.Random

class ViewScheduleAdapter(
    private val scheduleFragmentScreen: ScheduleFragmentScreen,
    private val context: Context?,
    private var viewScheduleModelList: List<ViewScheduleModel>,
    private val scheduleFragmentScreenViewModel: ScheduleFragmentScreenViewModel
) : RecyclerView.Adapter<ViewScheduleAdapter.ViewHolder>() {

    private var viewScheduleModel: ViewScheduleModel? = null

    private var dataList: List<ViewScheduleModel> = viewScheduleModelList.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterScheduleBinding: AdapterScheduleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_schedule,
            parent,
            false
        )
        return ViewHolder(adapterScheduleBinding)

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewScheduleModel = viewScheduleModelList[position]
        val randomColor =
            Color.parseColor(Constants.studentCategoriesBackGroundColor[Random.nextInt(Constants.studentCategoriesBackGroundColor.size)])
        holder.binding.startView.setBackgroundColor(randomColor)
        holder.binding.classNameTv.text =
            viewScheduleModel!!.course + " " + viewScheduleModel!!.section
        holder.binding.scheduleContentTv.text =
            viewScheduleModel!!.from_time + "- " + viewScheduleModel!!.to_time
        holder.binding.scheduleEditIcon.setOnClickListener { v ->
            viewScheduleModel = viewScheduleModelList[position]
            showPopup(
                v,
                viewScheduleModel!!.id,
                viewScheduleModel!!.degree,
                viewScheduleModel!!.course,
                viewScheduleModel!!.semester,
                viewScheduleModel!!.section,
                viewScheduleModel!!.from_time,
                viewScheduleModel!!.to_time,
                viewScheduleModel!!.notes,
                holder
            )
        }
    }

    override fun getItemCount(): Int {
        return viewScheduleModelList.size
    }

    class ViewHolder(binding: AdapterScheduleBinding) : RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterScheduleBinding

        init {
            this.binding = binding
        }
    }

    private fun showPopup(
        v: View,
        scheduleId: String,
        degree: String,
        course: String,
        semester: String,
        section: String,
        fromTime: String,
        toTime: String,
        notes: String,
        viewHolder: ViewScheduleAdapter.ViewHolder
    ) {
        val inflater = LayoutInflater.from(context)
        val popupBinding: UpdateSchedulePopupBinding = DataBindingUtil.inflate(
            inflater,
            com.desss.collegeproduct.R.layout.update_schedule_popup,
            null,
            false
        )
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0)
        popupWindow.setFocusable(true)
        popupWindow.update()
        setSpinnerAdapter(popupBinding.degreeSpinner, degree)
        setSpinnerAdapter(popupBinding.departmentSpinner, course)
        setSpinnerAdapter(popupBinding.semesterSpinner, semester)
        setSpinnerAdapter(popupBinding.sectionSpinner, section)
        popupBinding.etFromTime.setText(fromTime)
        popupBinding.etToTime.setText(toTime)
        popupBinding.inputMessage.setText(notes)
        popupBinding.btnClose.setOnClickListener {
            popupWindow.dismiss()
        }
        popupBinding.etFromTime.setOnClickListener {
            showTimePickerDialog(context!!, popupBinding.etFromTime)
        }

        popupBinding.etToTime.setOnClickListener {
            showTimePickerDialog(context!!, popupBinding.etToTime)
        }
        popupBinding.btnSave.setOnClickListener {
            if (!CommonValidation.isEditTextNotEmpty(popupBinding.etFromTime)) {
                popupBinding.etFromTime.error = "Enter From Time"
            } else if (!CommonValidation.isEditTextNotEmpty(popupBinding.etToTime)) {
                popupBinding.etToTime.error = "Enter To Time"
            } else if (!CommonValidation.isEditTextNotEmpty(popupBinding.inputMessage)) {
                popupBinding.inputMessage.error = "Enter Schedule Messsage"
            } else {
                CommonUtility.showProgressDialog(context)
                scheduleFragmentScreenViewModel.updateScheduleApi(
                    context as Activity,
                    "update",
                    "schedules",
                    scheduleId,
                    popupBinding.etFromTime.text.toString(),
                    popupBinding.etToTime.text.toString(),
                    popupBinding.inputMessage.text.toString()
                )
                observeViewModel(scheduleFragmentScreenViewModel, viewHolder, popupWindow)
            }
        }
    }

    private fun setSpinnerAdapter(spinner: Spinner, value: String) {
        val valuesList = listOf(value)
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, valuesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.isEnabled = false
    }

    private fun observeViewModel(
        viewModel: ScheduleFragmentScreenViewModel,
        viewHolder: ViewScheduleAdapter.ViewHolder,
        popupWindow: PopupWindow
    ) {
        val lifecycleOwner = (viewHolder.itemView.context as? LifecycleOwner) ?: return

        viewModel.getUpdateScheduleApiValuesData()
            ?.observe(lifecycleOwner, Observer { updateSchedule ->
                if (updateSchedule != null) {
                    if (updateSchedule.status == 400 && updateSchedule.data.isNotEmpty()) {
                        CommonUtility.cancelProgressDialog(context)
                        popupWindow.dismiss()
                    } else {
                        CommonUtility.toastString("Successfully Updated..!", context)
                        CommonUtility.cancelProgressDialog(context)
                        popupWindow.dismiss()
                        scheduleFragmentScreen.callScheduleApi()
                    }
                } else {
                    CommonUtility.cancelProgressDialog(context)
                    popupWindow.dismiss()
                }
            })
    }

    @SuppressLint("SetTextI18n")
    fun showTimePickerDialog(context: Context, editText: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val seconds = ":00"
        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime + seconds)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        viewScheduleModelList = if (query.isEmpty()) {
            dataList
        } else {
            dataList.filter {
                it.course.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }

}
package com.desss.collegeproduct.module.studentSubModule.notes.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentNotesScreenBinding
import com.desss.collegeproduct.module.studentSubModule.notes.adapter.NotesAdapter
import com.desss.collegeproduct.module.studentSubModule.notes.model.NotesModel
import com.desss.collegeproduct.module.studentSubModule.notes.viewModel.NotesFragmentScreenViewModel

class NotesFragmentScreen : Fragment() {

    private lateinit var fragmentNotesScreenBinding: FragmentNotesScreenBinding

    private lateinit var notesFragmentScreenViewModel: NotesFragmentScreenViewModel

    private var notesList = arrayListOf<NotesModel>()

    private var notesAdapter: NotesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentNotesScreenBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notes_screen, container, false)
        initViewModel()
        callApi()
        observeViewModel(notesFragmentScreenViewModel)
        return fragmentNotesScreenBinding.root
    }

    private fun initViewModel() {
        notesFragmentScreenViewModel =
            NotesFragmentScreenViewModel(activity?.application!!, requireActivity())
    }

    private fun callApi() {
        CommonUtility.showProgressDialog(context)
        notesFragmentScreenViewModel.callNotesApi(
            requireActivity(),
            "student_notes",
            SharedPref.getDegree(context).toString(),
            SharedPref.getCourse(context).toString(),
            SharedPref.getSemester(context).toString(),
            SharedPref.getSection(context).toString()
        )
    }

    private fun observeViewModel(viewModel: NotesFragmentScreenViewModel) {

        viewModel.getNotesData()?.observe(requireActivity(), Observer { notesData ->
            if (notesData != null) {
                if (notesData.status == 403 && notesData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                    fragmentNotesScreenBinding.rlError.visibility = View.VISIBLE
                } else {
                    fragmentNotesScreenBinding.rlError.visibility = View.GONE
                    handleSyllabusData(notesData)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
                fragmentNotesScreenBinding.rlError.visibility = View.VISIBLE
            }
        })
    }

    private fun handleSyllabusData(syllabusData: CommonResponseModel<NotesModel>?) {
        notesList.clear()
        syllabusData?.let {
            val filteredItems = it.data.filter { syllabus ->
                syllabus.status == "1" && syllabus.is_deleted == "0"
            }
            notesList.addAll(filteredItems)
            setBindingAdapter(notesList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setBindingAdapter(notesModel: List<NotesModel>) {
        notesAdapter = NotesAdapter(context, notesModel)
        fragmentNotesScreenBinding.recyclerView.adapter = notesAdapter
        notesAdapter!!.notifyDataSetChanged()
        CommonUtility.cancelProgressDialog(context)
    }

}
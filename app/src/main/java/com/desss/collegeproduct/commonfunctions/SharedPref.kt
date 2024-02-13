package com.desss.collegeproduct.commonfunctions

import android.content.Context

object SharedPref {
    fun setId(context: Context, id: String?) {
        SharedPrefUtils.putString(context, "id", id)
    }

    fun getId(context: Context?): String? {
        return SharedPrefUtils.getString(context, "id")
    }

    fun setEmailId(context: Context, emailId: String?) {
        SharedPrefUtils.putString(context, "emailId", emailId)
    }

    fun getEmailId(context: Context?): String? {
        return SharedPrefUtils.getString(context, "emailId")
    }

    fun setFirstName(context: Context, firstName: String?) {
        SharedPrefUtils.putString(context, "firstName", firstName)
    }

    fun getFirstName(context: Context?): String? {
        return SharedPrefUtils.getString(context, "firstName")
    }

    fun setLastName(context: Context, lastName: String?) {
        SharedPrefUtils.putString(context, "lastName", lastName)
    }

    fun getLastName(context: Context?): String? {
        return SharedPrefUtils.getString(context, "lastName")
    }

    fun setRollId(context: Context, rollId: String?) {
        SharedPrefUtils.putString(context, "rollId", rollId)
    }

    fun getRollId(context: Context?): String? {
        return SharedPrefUtils.getString(context, "rollId")
    }

    fun setRegisterNo(context: Context, registerNo: String?) {
        SharedPrefUtils.putString(context, "registerNo", registerNo)
    }

    fun getRegisterNo(context: Context?): String? {
        return SharedPrefUtils.getString(context, "registerNo")
    }

    fun setDegree(context: Context, degree: String?) {
        SharedPrefUtils.putString(context, "degree", degree)
    }

    fun getDegree(context: Context?): String? {
        return SharedPrefUtils.getString(context, "degree")
    }

    fun setCourse(context: Context, course: String?) {
        SharedPrefUtils.putString(context, "course", course)
    }

    fun getCourse(context: Context?): String? {
        return SharedPrefUtils.getString(context, "course")
    }

    fun setSemester(context: Context, semester: String?) {
        SharedPrefUtils.putString(context, "semester", semester)
    }

    fun getSemester(context: Context?): String? {
        return SharedPrefUtils.getString(context, "semester")
    }

    fun setSection(context: Context, section: String?) {
        SharedPrefUtils.putString(context, "section", section)
    }

    fun getSection(context: Context?): String? {
        return SharedPrefUtils.getString(context, "section")
    }
}
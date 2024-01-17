package com.desss.collegeproduct.commonfunctions

import android.content.Context

object SharedPref {
    fun setCompanyId(context: Context, companyId: String?) {
        SharedPrefUtils.putString(context, "companyId", companyId)
    }

    fun getCompanyId(context: Context?): String? {
        return SharedPrefUtils.getString(context, "companyId")
    }

    fun setEmployeeId(context: Context, employeeId: Int) {
        SharedPrefUtils.putInt(context, "employeeId", employeeId)
    }

    fun getEmployeeId(context: Context?): Int {
        return SharedPrefUtils.getInt(context, "employeeId")
    }
}
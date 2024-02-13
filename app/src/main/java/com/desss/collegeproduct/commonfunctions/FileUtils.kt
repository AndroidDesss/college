package com.desss.collegeproduct.commonfunctions

import android.content.Intent

object FileUtils {
    fun createPdfFilePickerIntent(): Intent {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }
}
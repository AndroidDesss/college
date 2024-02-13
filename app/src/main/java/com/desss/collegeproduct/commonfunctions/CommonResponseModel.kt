package com.desss.collegeproduct.commonfunctions

data class CommonResponseModel<T>(
    val code: Int,
    val status: Int,
    val `data`: List<T>,
    val parent: String
)



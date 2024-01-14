package com.desss.collegeproduct.repository.service

import retrofit2.Response


interface ApiResponseListener {
    fun onReceiveResult(request: String, response: Response<*>)
    fun onReceiveError(request: String, error: String)
}
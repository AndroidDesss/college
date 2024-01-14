package com.desss.collegeproduct.module.splash


import android.content.Context
import android.view.View
import com.desss.collegeproduct.repository.model.requestModel.LoginRequestModel
import com.desss.collegeproduct.repository.model.responseModel.LoginModel
import com.desss.collegeproduct.repository.service.ApiResponseListener
import com.desss.collegeproduct.repository.service.ApiUtils
import com.desss.collegeproduct.repository.service.retrofit.Request
import retrofit2.Call
import retrofit2.Response
import java.util.*

class SplashScreenViewModel (val context: Context) : Observable() , ApiResponseListener {

    fun callLoginApi(appKey:String){
        try{
            val loginRequestModel =
                    LoginRequestModel(
                            appKey
                    )

            val retroApiCall = RetrofitConnect.getInstance()?.getRetrofitApiService(ApiUtils.COLLEGE_BASE_URL)
            val postRes: Call<LoginModel?> = retroApiCall?.getLogin(ApiUtils.LOGIN,loginRequestModel)!!
            RetrofitConnect.getInstance()?.addRetrofitCalls(postRes, this, Request.LOGIN)

        }catch (e:Exception){
                e.printStackTrace()
        }
    }


//    override fun onReceiveResult(request: String, response: Response<*>) {
//        when (request) {
//
//            Request.LOGIN -> {
//
//                val loginModel =
//                    RetrofitConnect.getInstance()?.dataConvertor(response, LoginModel::class.java)
//
//                if (loginModel!!.loginInformation?.status != null) {
//                    val userId: String? = response.headers()["userId"]
//                    val amsCustomerId: String? = response.headers()["amsCustomerId"]
//                    context?.let {
//                        StoreSharedPrefData.INSTANCE.savePrefValue(
//                            ApiUtils.USERID, userId,
//                            it
//                        )
//                    }
//                    context?.let {
//                        StoreSharedPrefData.INSTANCE.savePrefValue(
//                            ApiUtils.AMSCUSTOMERID, amsCustomerId,
//                            it
//                        )
//                    }
//                    setChanged()
//                    notifyObservers(loginModel)
//                }
//            }
//
//            Request.LOGOUT -> {
//                val logoutModel = RetrofitConnect.getInstance()?.dataConvertor(response, LogoutResponseModel::class.java)
//                setChanged()
//                notifyObservers(logoutModel)
//            }
//
//            Request.NOOFRECORDS -> {
//                val countModel = RetrofitConnect.getInstance()?.dataConvertor(response, OrderHistorycount::class.java)
//                setChanged()
//                notifyObservers(countModel)
//            }
//
//
//            Request.VERSION -> {
//                val versionModel = RetrofitConnect.getInstance()?.dataConvertor(response, VersionResponseModel::class.java)
//                setChanged()
//                notifyObservers(versionModel)
//            }
//          }
//    }
//
//    override fun onReceiveError(request: String, error: String) {
//        if (error != null) {
//            setChanged()
//            notifyObservers(ErrorHandler(0, error))
//        } else {
//            setChanged()
//            notifyObservers(ErrorHandler(0, R.string.service_fail))
//        }
//    }
}
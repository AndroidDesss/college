package com.desss.collegeproduct.paymentGateway

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.Constants
import com.desss.collegeproduct.databinding.ActivityPaymentScreenBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentScreen : AppCompatActivity(), PaymentResultListener {

    private lateinit var paymentScreenBinding: ActivityPaymentScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentScreenBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_payment_screen)
        Checkout.preload(applicationContext)
        initListener()
    }

    private fun initListener() {
        paymentScreenBinding.btnPay.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnPay -> {
                val amount = 100  // amount should be in paisa
                val appName = this.applicationInfo.loadLabel(this.packageManager).toString()
                val description = "Exam Fees"
                startRazorpayPayment(appName, description, amount)
            }
        }
    }

    private fun startRazorpayPayment(
        appNameValue: String,
        descriptionValue: String,
        amountValue: Int
    ) {
        val checkout = Checkout()
        checkout.setKeyID(Constants.RazorPayKeyId)
        try {
            val options = JSONObject()
            options.put("name", appNameValue)
            options.put("description", descriptionValue)
            options.put("currency", "INR")
            options.put("amount", amountValue)
            checkout.open(this, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        CommonUtility.toastString("Payment Success..!", this@PaymentScreen)
    }

    override fun onPaymentError(code: Int, response: String?) {
        CommonUtility.toastString("Payment Failed..!", this@PaymentScreen)
    }
}
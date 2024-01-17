package com.desss.collegeproduct.commonfunctions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.desss.collegeproduct.R
import java.text.SimpleDateFormat
import java.util.Date


class CommonUtility(private val context: Context) {

    interface DialogClickListener {
        fun dialogOkBtnClicked(value: String?)
        fun dialogNoBtnClicked(value: String?)
    }

    companion object {

        //To Get CurrentDate
        @SuppressLint("SimpleDateFormat")
        public fun getCurrentDate(mContext: Context?): String {
            val curFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateObj = Date()
            return curFormat.format(dateObj)
        }

        //Toast a Message
        public fun toastString(toastText: String?, context: Context?) {
            showToastContent(toastText, 2000, context)
        }

        public fun showToastContent(text: String?, duration: Int, context: Context?) {
            val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 100)
            toast.show()
            val handler = Handler()
            handler.postDelayed({ toast.cancel() }, duration.toLong())
        }

        //Show AlertDialogue
        public fun showAlertDialog(context: Context?, title: String?, message: String?, positiveBtnText: String?, negativeBtnText: String?, dialogClickListener: DialogClickListener)
        {
            val alertDialogBuilder = AlertDialog.Builder(context)
            if (title != null && title.length > 0) alertDialogBuilder.setTitle(title)
            if (message != null && message.length > 0) alertDialogBuilder.setMessage(message)
            alertDialogBuilder.setPositiveButton(positiveBtnText, null)
            if (negativeBtnText != null) {
                alertDialogBuilder.setNegativeButton(negativeBtnText, null)
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setOnShowListener {
                val okBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                okBtn.setTextColor(ContextCompat.getColor(context!!, R.color.black))
                okBtn.setTextAppearance(R.style.alertFont)
                okBtn.setOnClickListener {
                    dialogClickListener.dialogOkBtnClicked("")
                    alertDialog.dismiss()
                }
                val cancelBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                cancelBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                cancelBtn.setTextColor(ContextCompat.getColor(context, R.color.black))
                cancelBtn.setTextAppearance(R.style.alertFont)
                cancelBtn.setOnClickListener {
                    dialogClickListener.dialogNoBtnClicked("")
                    alertDialog.dismiss()
                }
            }
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.setCancelable(false)
            alertDialog.show()
        }


        //To Check NetWorkAvailability
        public fun isNetworkAvailable(act: Activity): Boolean {
            val connectivityManager = act.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        //To Start ActivityClearStack
        public fun commonStartActivityClearStack(from: Activity, toActivity: Class<*>?, bundle: Bundle?, finishActivityOrNot: Boolean) {
            val intent = Intent(from, toActivity)
            if (bundle != null)
            {
                intent.putExtras(bundle)
            }
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            from.startActivity(intent)
            if (finishActivityOrNot)
            {
                from.finish()
            }
        }

        //To Start Activity
        public fun commonStartActivity(from: Activity,toActivity: Class<*>?,bundle: Bundle?,finishActivityOrNot: Boolean) {
            val intent = Intent(from, toActivity)
            if (bundle != null)
            {
                intent.putExtras(bundle)
            }
            from.startActivity(intent)
            if (finishActivityOrNot)
            {
                from.finish()
            }
        }

        //To Start Fragment
        public fun navigateToFragment(fragmentManager: FragmentManager, destinationFragment: Fragment, containerId: Int, addToBackStack: Boolean) {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(containerId, destinationFragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }

//        private fun navigateToFragmentB() {
//            val fragmentB = FragmentB()
//            FragmentUtils.navigateToFragment(requireActivity().getSupportFragmentManager(),fragmentB,R.id.fragment_container,true)
//        }


    }

}
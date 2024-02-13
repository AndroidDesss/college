package com.desss.collegeproduct.commonfunctions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.Gravity
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.desss.collegeproduct.BuildConfig
import com.desss.collegeproduct.R
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import java.io.File
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
        fun getCurrentDate(mContext: Context?): String {
            val curFormat = SimpleDateFormat("dd-MM-yyyy")
            val dateObj = Date()
            return curFormat.format(dateObj)
        }

        //Toast a Message
        fun toastString(toastText: String?, context: Context?) {
            showToastContent(toastText, 2000, context)
        }

        fun showToastContent(text: String?, duration: Int, context: Context?) {
            val toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 100)
            toast.show()
            val handler = Handler()
            handler.postDelayed({ toast.cancel() }, duration.toLong())
        }

        //Show AlertDialogue
        fun showAlertDialog(
            context: Context?,
            title: String?,
            message: String?,
            positiveBtnText: String?,
            negativeBtnText: String?,
            dialogClickListener: DialogClickListener
        ) {
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
        fun isNetworkAvailable(act: Activity): Boolean {
            val connectivityManager =
                act.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

        //To Start ActivityClearStack
        fun commonStartActivityClearStack(
            from: Activity,
            toActivity: Class<*>?,
            bundle: Bundle?,
            finishActivityOrNot: Boolean
        ) {
            val intent = Intent(from, toActivity)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            from.startActivity(intent)
            if (finishActivityOrNot) {
                from.finish()
            }
        }

        //To Start Activity
        fun commonStartActivity(
            from: Activity,
            toActivity: Class<*>?,
            bundle: Bundle?,
            finishActivityOrNot: Boolean
        ) {
            val intent = Intent(from, toActivity)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            from.startActivity(intent)
            if (finishActivityOrNot) {
                from.finish()
            }
        }

        //To Start Fragment
        fun navigateToFragment(
            fragmentManager: FragmentManager,
            destinationFragment: Fragment,
            containerId: Int,
            addToBackStack: Boolean
        ) {
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(containerId, destinationFragment)
            if (addToBackStack) {
                transaction.addToBackStack(null)
            }
            transaction.commit()
        }

        fun navigateToFragmentWithBundle(
            fragmentManager: FragmentManager,
            fragment: Fragment,
            bundle: Bundle,
            containerId: Int,
            addToBackStack: Boolean
        ) {
            fragment.arguments = bundle

            val transaction = fragmentManager.beginTransaction()
                .replace(containerId, fragment)

            if (addToBackStack) {
                transaction.addToBackStack(null)
            }

            transaction.commit()
        }

        fun showProgressDialog(activity: Context?) {
            try {
                cancelProgressDialog(activity)
                if (Constants.dialog == null || !Constants.dialog!!.isShowing) {
                    Constants.dialog = activity?.let { Dialog(it) }
                    Constants.dialog?.setContentView(R.layout.layout_progress_dialog)
                    Constants.dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    Constants.dialog?.setCancelable(false)
                    Constants.dialog?.show()
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }

        fun cancelProgressDialog(activity: Context?) {
            try {
                if (Constants.dialog != null && Constants.dialog!!.isShowing) {
                    Constants.dialog?.dismiss()
                    Constants.dialog = null
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }

        fun downloadPdf(activity: Context, pdfFileName: String, path: String, url: String) {
            val downloader = PRDownloader.download(url, path, pdfFileName)
                .build()
                .setOnStartOrResumeListener { }.setOnPauseListener { }.setOnCancelListener { }
                .setOnProgressListener { progress ->
                }.start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        cancelProgressDialog(activity)
                        toastString("Download Completed..!", activity)
                    }

                    override fun onError(error: Error) {
                        cancelProgressDialog(activity)
                        toastString("Download Failed..!", activity)
                    }
                })
        }

        fun createAppFolder(context: Context): File {
            val documentsDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
            val pdfFile = File(documentsDir, appName)
            if (!pdfFile.exists()) {
                pdfFile.mkdirs()
            }
            return pdfFile
        }

        fun checkStoragePermission(context: Context): Boolean {
            val readPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val writePermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED
        }

        fun requestStoragePermission(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1001
            )
        }
    }

}
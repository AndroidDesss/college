package com.desss.collegeproduct.module.commonDashBoardFragment.notification.adapter

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.AdapterNotificationBinding
import com.desss.collegeproduct.module.commonDashBoardFragment.notification.model.NotificationModel

class NotificationAdapter(
    private val context: Context?,
    private val notificationModelList: List<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    private var notificationModel: NotificationModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterNotificationBinding: AdapterNotificationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.adapter_notification, parent, false
        )
        return ViewHolder(adapterNotificationBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        notificationModel = notificationModelList[position]
        //Read More and Read Less

        val originalText = notificationModel?.text ?: ""
        val maxLength = 100
        val shortText = if (originalText.length > maxLength) {
            originalText.substring(0, maxLength) + "... Read More"
        } else {
            originalText
        }

        var isFullTextShown = false
        val spannableString = SpannableStringBuilder(shortText)
        val readMoreSpan = ForegroundColorSpan(Color.BLUE)
        val readLessSpan = ForegroundColorSpan(Color.RED)

// Set "Read More" span
        if (shortText.contains("Read More")) {
            spannableString.setSpan(readMoreSpan, shortText.indexOf("Read More"), shortText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        holder.binding.notificationContentTv.text = spannableString

        holder.binding.notificationContentTv.setOnClickListener {
            isFullTextShown = !isFullTextShown
            if (isFullTextShown) {
                // Display full text with "Read Less"
                val fullText = originalText + " Read Less"
                val fullSpannableString = SpannableStringBuilder(fullText)
                fullSpannableString.setSpan(readLessSpan, fullText.indexOf("Read Less"), fullText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                holder.binding.notificationContentTv.text = fullSpannableString
            } else {
                // Display shortened text with "Read More"
                val newShortText = if (originalText.length > maxLength) {
                    originalText.substring(0, maxLength) + "... Read More"
                } else {
                    originalText
                }
                val newSpannableString = SpannableStringBuilder(newShortText)
                if (newShortText.contains("Read More")) {
                    newSpannableString.setSpan(readMoreSpan, newShortText.indexOf("Read More"), newShortText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                holder.binding.notificationContentTv.text = newSpannableString
            }
        }

        val dateArray = notificationModel!!.date.split(" ").toTypedArray()
        holder.binding.notificationDateTv.text = dateArray[0]
        holder.binding.notificationTimeTv.text = dateArray[1]
    }

    override fun getItemCount(): Int {
        return notificationModelList.size
    }

    class ViewHolder(binding: AdapterNotificationBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        val binding: AdapterNotificationBinding

        init {
            this.binding = binding
        }
    }

}
package com.desss.collegeproduct.module.studentSubModule.meetings


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.desss.collegeproduct.R
import com.desss.collegeproduct.databinding.FragmentMeetingFragmentsBinding


class MeetingFragments : Fragment() {

    private lateinit var fragmentMeetingFragmentsBinding: FragmentMeetingFragmentsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentMeetingFragmentsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_meeting_fragments, container, false)
        initListener()
        return fragmentMeetingFragmentsBinding.root
    }

    private fun initListener() {
        fragmentMeetingFragmentsBinding.btnZoom.setOnClickListener(onClickListener)
        fragmentMeetingFragmentsBinding.btnGoogleMeet.setOnClickListener(onClickListener)
        fragmentMeetingFragmentsBinding.btnTeams.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btnZoom ->  {
                joinZoomMeeting("https://us05web.zoom.us/j/86286739814?pwd=bqXHkEWeIY6yMWeszKRRVvOBma406V.1")
            }
            R.id.btnGoogleMeet -> {
                joinGoogleMeet("obq-hyrm-gfr")
            }
            R.id.btnTeams -> {
                joinTeamsMeeting("")
            }
        }
    }

    private fun joinZoomMeeting(meetingLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meetingLink))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=us.zoom.videomeetings"))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // If Play Store app is not installed, open the browser
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=us.zoom.videomeetings"))
                startActivity(intent)
            }
        }
    }

    private fun joinGoogleMeet(meetingLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://meet.google.com/$meetingLink"))
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.google.android.apps.meetings"))
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.meetings"))
                startActivity(intent)
            }
        }
    }

    private fun joinTeamsMeeting(meetingLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("msteams://teams.microsoft.com/l/meeting/$meetingLink"))
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.microsoft.teams"))
            startActivity(playStoreIntent)
        }
    }


}
package com.desss.collegeproduct.module.studentSubModule.Lms.fragment

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonResponseModel
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentLmsVideoExamBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.model.LmsDurationModel
import com.desss.collegeproduct.module.studentSubModule.Lms.viewModel.LmsLessonScreenViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.Locale
class LmsVideoFragment : Fragment() {

    private lateinit var fragmentLmsVideoExamBinding: FragmentLmsVideoExamBinding

    private lateinit var player: SimpleExoPlayer

    private var currentVideoIndex = 0

    private val watchedSecondsSet = HashSet<Int>()

    private val handler = Handler()

    private var isTrackingProgress = false

    private var videoDurationSeconds: Long = 0

    private var isNewVideoLoaded = false

    private var videoUrls: ArrayList<String>? = null

    private var lessonIds: ArrayList<String>? = null

    private var currentPosition: Int = -1

    var isFullscreen = false

    private var lastWatchedTime: String = "00:00:00"

    private lateinit var lmsLessonScreenViewModel: LmsLessonScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            videoUrls = bundle.getStringArrayList("videoUrls")
            lessonIds = bundle.getStringArrayList("lessonIds")
            currentPosition = bundle.getInt("position",-1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLmsVideoExamBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_lms_video_exam, container, false)
        initViewModel()
        initListener()
        fragmentLmsVideoExamBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val newPosition = player.duration * progress / 100
                    player.seekTo(newPosition)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        return fragmentLmsVideoExamBinding.root
    }

    private fun initViewModel() {
        lmsLessonScreenViewModel =
            LmsLessonScreenViewModel(activity?.application!!, requireActivity())
    }



    private fun initListener() {
        fragmentLmsVideoExamBinding.btnPrevious.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.btnBackward.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.btnPlayPause.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.btnForward.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.btnNext.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.btnFullscreen.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.btnAttendTest.setOnClickListener(onClickListener)
        fragmentLmsVideoExamBinding.orientationLayout.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.btn_previous ->
            {
                loadPreviousVideo()
            }
            R.id.btn_next ->
            {
                loadNextVideo()
            }
            R.id.btn_backward ->
            {
                seekBackward(5000)
            }
            R.id.btn_forward ->
            {
                seekForward(5000)
            }
            R.id.btn_play_pause ->
            {
                if (player.playWhenReady) {
                    pauseVideo()
                } else {
                    playVideo()
                }
            }
            R.id.btnAttendTest ->
            {
                val id = getCurrentLessonId()
                val bundle = Bundle().apply {
                    putString("lessonId", id)
                }

                val lmsExamFragmentScreen = LMSExamFragmentScreen()
                lmsExamFragmentScreen.arguments = bundle
                CommonUtility.navigateToFragment(
                    (context as FragmentActivity).supportFragmentManager,
                    lmsExamFragmentScreen,
                    R.id.container,
                    true
                )
            }
            R.id.btn_fullscreen ->
            {
                if (!isFullscreen) {
                    val orientationLayoutParams = fragmentLmsVideoExamBinding.orientationLayout.layoutParams
                    orientationLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    orientationLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    fragmentLmsVideoExamBinding.orientationLayout.layoutParams = orientationLayoutParams
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    isFullscreen = true
                } else {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    isFullscreen = false
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlayer()
        playVideoAtIndex(currentPosition)
    }

    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector()
        val renderersFactory = DefaultRenderersFactory(requireContext())
        player = SimpleExoPlayer.Builder(requireContext(), renderersFactory)
            .setTrackSelector(trackSelector)
            .build()
        fragmentLmsVideoExamBinding.playerView.player = player
        player.addListener(playerListener)
    }

    private fun loadNextVideo() {
        if (currentVideoIndex < videoUrls!!.size - 1) {
            getCurrentLessonId()?.let { lessonId ->
                callPostDurationApi(lessonId)
            }
            currentPosition = currentVideoIndex + 1
            playVideoAtIndex(currentVideoIndex + 1)
            currentVideoIndex++
        } else
        {
            CommonUtility.toastString("No Next video..!", activity)
        }
    }

    private fun loadPreviousVideo() {
        if (currentVideoIndex > 0) {
            getCurrentLessonId()?.let { lessonId ->
                callPostDurationApi(lessonId)
            }
            currentPosition = currentVideoIndex - 1
            playVideoAtIndex(currentVideoIndex - 1)
            currentVideoIndex--
        }else
        {
            CommonUtility.toastString("No Previous video..!", activity)
        }
    }

    private fun seekForward(durationMillis: Long) {
        val currentPosition = player.currentPosition
        val newPosition = currentPosition + durationMillis
        player.seekTo(newPosition.coerceAtMost(player.duration))
    }

    private fun seekBackward(durationMillis: Long) {
        val currentPosition = player.currentPosition
        val newPosition = currentPosition - durationMillis
        player.seekTo(newPosition.coerceAtLeast(0))
    }

    private fun playVideo() {
        player.playWhenReady = true
        fragmentLmsVideoExamBinding.btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
    }

    private fun pauseVideo() {
        player.playWhenReady = false
        fragmentLmsVideoExamBinding.btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
    }

    private fun playVideoAtIndex(index: Int) {
        watchedSecondsSet.clear()
        val mediaUrl = videoUrls!![index]
        val id = getCurrentLessonId()
        callApi(id.toString())
        observeViewModel(lmsLessonScreenViewModel,mediaUrl,index)
    }

    private fun updatePreviousButtonVisibility() {
        fragmentLmsVideoExamBinding.btnPrevious.isEnabled = if (currentVideoIndex > 0) true else false
    }

    override fun onStart() {
        super.onStart()
        player.playWhenReady = true
        startTrackingProgress()
    }

    override fun onStop() {
        super.onStop()
        getCurrentLessonId()?.let { lessonId ->
            callPostDurationApi(lessonId)
        }
        player.playWhenReady = false
        stopTrackingProgress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        getCurrentLessonId()?.let { lessonId ->
            callPostDurationApi(lessonId)
        }
    }

    private fun startTrackingProgress() {
        if (!isTrackingProgress) {
            isTrackingProgress = true
            handler.post(updateProgressTask)
        }
    }

    private fun stopTrackingProgress() {
        isTrackingProgress = false
        handler.removeCallbacks(updateProgressTask)
    }

    private val playerListener = object : Player.EventListener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if (isPlaying) {
                startTrackingProgress()
            } else {
                stopTrackingProgress()
            }
        }
        override fun onTimelineChanged(timeline: Timeline, reason: Int) {
            super.onTimelineChanged(timeline, reason)
            videoDurationSeconds = player.duration / 1000
            if (isNewVideoLoaded) {
                isNewVideoLoaded = false
            }
        }
        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)
            when (playbackState) {
                Player.STATE_BUFFERING -> CommonUtility.showProgressDialog(context)
                Player.STATE_READY, Player.STATE_ENDED -> CommonUtility.cancelProgressDialog(context)
            }
        }
        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
            updateSeekBar()
        }
    }

    private val updateProgressTask = object : Runnable {
        override fun run() {
            updateSeekBar()
            val currentSeconds = (player.currentPosition / 1000).toInt()
            watchedSecondsSet.add(currentSeconds)
            handler.postDelayed(this, 1000) // Update every 1 second
            val twentyPercentDuration = (videoDurationSeconds * 0.01).toInt()
            if(watchedSecondsSet.size >= twentyPercentDuration)
            {
                fragmentLmsVideoExamBinding.btnNext.isClickable = true
                fragmentLmsVideoExamBinding.btnAttendTest.visibility = View.VISIBLE
            }
            else
            {
                fragmentLmsVideoExamBinding.btnNext.isClickable = false
                fragmentLmsVideoExamBinding.btnAttendTest.visibility = View.INVISIBLE
            }
        }
    }
    private fun updateSeekBar() {
        val duration = player.duration
        val position = player.currentPosition
        if (duration >= 0) {
            val startTime = formatTime(position)
            val endTime = formatTime(duration)

            fragmentLmsVideoExamBinding.videoStartTime.text = startTime
            fragmentLmsVideoExamBinding.videoEndTime.text = endTime
            val percentage = (position * 100 / duration).toInt()
            fragmentLmsVideoExamBinding.seekBar.progress = percentage
        }
        updateLastWatchedSeconds(position)
    }

    private fun updateLastWatchedSeconds(currentPosition: Long) {
        lastWatchedTime = formatTime(currentPosition)
    }


    private fun formatTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
    }

    fun handleBackPressed(): Boolean {
        if (isFullscreen) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            isFullscreen = false
            return true
        }
        else
        {
            return false
        }
    }

    private fun getCurrentLessonId(): String? {
        return if (currentPosition != -1 && lessonIds != null && currentPosition < lessonIds!!.size) {
            lessonIds!![currentPosition]
        } else {
            null
        }
    }

    private fun callApi(videoId: String) {
        CommonUtility.showProgressDialog(context)
        lmsLessonScreenViewModel.callLmsVideoDurationApi(
            requireActivity(), "get_lms_duration", SharedPref.getId(context).toString(),
            videoId)
    }

    private fun observeViewModel(viewModel: LmsLessonScreenViewModel,mediaUrl: String,index: Int) {
        viewModel.getLmsVideoDurationData()?.observe(requireActivity(), Observer { lmsDurationData ->
            if (lmsDurationData != null) {
                if (lmsDurationData.status == 401 && lmsDurationData.data.isNotEmpty()) {
                    CommonUtility.cancelProgressDialog(activity)
                } else {
                    handleLmsDurationData(lmsDurationData,mediaUrl,index)
                }
            } else {
                CommonUtility.cancelProgressDialog(activity)
            }
        })
    }

    private fun handleLmsDurationData(response: CommonResponseModel<LmsDurationModel>,mediaUrl: String,index: Int) {
        if (response.status == 200) {
            val durationDataList: List<LmsDurationModel> = response.data
            val userProfile: LmsDurationModel? = durationDataList.firstOrNull()

            userProfile?.let {
                val dataSourceFactory = DefaultDataSourceFactory(requireContext(), Util.getUserAgent(requireContext(), "YourApplicationName"))
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(mediaUrl))
                player.prepare(mediaSource)
                val durationString = it.duration
                val durationParts = durationString.split(":")
                val hours = durationParts[0].toLong()
                val minutes = durationParts[1].toLong()
                val seconds = durationParts[2].toLong()
                val seekDuration = hours * 3600 + minutes * 60 + seconds
                player.seekTo(seekDuration * 1000)
                isNewVideoLoaded = true
                currentPosition = index
                if(it.custom_duration!= null || it.custom_duration !="")
                {
                    val customDurationParts = it.custom_duration.replace("[", "").replace("]", "").split(",")
                    for (part in customDurationParts) {
                        watchedSecondsSet.add(part.trim().toIntOrNull() ?: 0)
                    }
                }
                updatePreviousButtonVisibility()
            }
        }
    }

    private fun callPostDurationApi(lessonId: String) {
        lmsLessonScreenViewModel.callLmsVideoPostDurationApi(
            requireActivity(), "update_duration_lms", SharedPref.getId(context).toString(),
            lessonId,lastWatchedTime, watchedSecondsSet.toString()
        )
    }

}
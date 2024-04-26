package com.desss.collegeproduct.module.studentSubModule.Lms

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.desss.collegeproduct.R
import com.desss.collegeproduct.commonfunctions.CommonUtility
import com.desss.collegeproduct.commonfunctions.SharedPref
import com.desss.collegeproduct.databinding.FragmentExpoPlayerScreenBinding
import com.desss.collegeproduct.module.studentSubModule.Lms.fragment.LMSExamFragmentScreen
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class ExpoPlayerFragmentScreen : Fragment() {


    private lateinit var player: SimpleExoPlayer

    private lateinit var fragmentExpoPlayerScreenBinding: FragmentExpoPlayerScreenBinding

    private val videoUrls = listOf(
        "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4"
    )

    private var currentVideoIndex = 0

    private val watchedSecondsSet = HashSet<Int>()

    private val handler = Handler()

    private var isTrackingProgress = false

    private var videoDurationSeconds: Long = 0


    private var isNewVideoLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentExpoPlayerScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expo_player_screen, container, false)
        initListener()
        return fragmentExpoPlayerScreenBinding.root
    }

    private fun initListener() {
        fragmentExpoPlayerScreenBinding.nextButton.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener { view ->
        when (view.id) {
            R.id.nextButton ->
            {
                loadNextVideo()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializePlayer()
        loadNextVideo()
    }

    private fun initializePlayer() {
        val trackSelector = DefaultTrackSelector()
        val renderersFactory = DefaultRenderersFactory(requireContext())
        player = SimpleExoPlayer.Builder(requireContext(), renderersFactory)
            .setTrackSelector(trackSelector)
            .build()
        fragmentExpoPlayerScreenBinding.playerView.player = player
        player.addListener(playerListener)
    }

    override fun onStart() {
        super.onStart()
        player.playWhenReady = true
        startTrackingProgress()
    }

    override fun onStop() {
        super.onStop()
        player.playWhenReady = false
        stopTrackingProgress()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
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
                watchedSecondsSet.clear()
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
    }

    private val updateProgressTask = object : Runnable {
        override fun run() {
            val currentSeconds = (player.currentPosition / 1000).toInt()
            watchedSecondsSet.add(currentSeconds)
            handler.postDelayed(this, 1000) // Update every 1 second
            val twentyPercentDuration = (videoDurationSeconds * 0.20).toInt()
            if(watchedSecondsSet.size >= twentyPercentDuration)
            {
                fragmentExpoPlayerScreenBinding.nextButton.visibility = View.VISIBLE

            }
            else
            {
                fragmentExpoPlayerScreenBinding.nextButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun loadNextVideo() {
        if (currentVideoIndex < videoUrls.size) {
            val mediaUrl = videoUrls[currentVideoIndex]
            val dataSourceFactory = DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), "YourApplicationName")
            )
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(mediaUrl))

            player.prepare(mediaSource)
            isNewVideoLoaded = true
            currentVideoIndex++
        }
        else
        {
            SharedPref.setIsAllVideoWatched(requireContext(), "yes")
            val lmsExamFragmentScreen = LMSExamFragmentScreen()
            CommonUtility.navigateToFragment(
                (context as FragmentActivity).supportFragmentManager,
                lmsExamFragmentScreen,
                R.id.container,
                false
            )
        }
    }
}
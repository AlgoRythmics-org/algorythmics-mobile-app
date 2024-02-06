package com.example.algorythmics.fragments.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.algorythmics.databinding.FragmentVideoBinding
import com.example.algorythmics.retrofit.models.VideoModel
import com.example.algorythmics.retrofit.repositories.VideoRepository
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

class VideoFragment : Fragment() {
    private lateinit var binding: FragmentVideoBinding
    private val videoRepository = VideoRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            try {
                val videos: List<VideoModel> = videoRepository.getAllVideo()
                if (videos.isNotEmpty()) {
                    val algorithmId = arguments?.getString("algorithmId")
                    val video = videos.find { it.algorithmId == algorithmId }
                    video?.let { loadVideo(it) } ?: showError("No video found for this algorithm")
                } else {
                    showError("No videos found")
                }
            } catch (e: Exception) {
                showError("Error loading videos: ${e.message}")
            }
        }
    }

    private fun loadVideo(video: VideoModel) {
        val youTubePlayerView: YouTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = getVideoIdFromLink(video.link)
                youTubePlayer.loadVideo(videoId, 0f)
            }
        })
    }

    private fun getVideoIdFromLink(videoLink: String): String {
        return videoLink.substringAfter("v=")
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
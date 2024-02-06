package com.example.algorythmics.retrofit.models

import com.google.gson.annotations.SerializedName

data class VideoModel(
    @SerializedName("_id")
    val videoId: String,
    @SerializedName("video_name")
    val videoName: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("algorithm_id")
    val algorithmId: String
)

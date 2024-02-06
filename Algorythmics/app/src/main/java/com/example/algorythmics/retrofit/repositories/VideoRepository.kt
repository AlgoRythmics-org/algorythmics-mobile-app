package com.example.algorythmics.retrofit.repositories

import android.util.Log
import com.example.algorythmics.retrofit.ApiClient
import com.example.algorythmics.retrofit.models.AlgorithmModel
import com.example.algorythmics.retrofit.models.VideoModel

class VideoRepository {
    suspend fun getOneVideoById(id: String): VideoModel? {
        try{
            return ApiClient.apiService.getOneVideoById(id)
        }catch (e : Exception){
            Log.e("xxx Repository", "getOneAlgorithmById Exception", e)
        }
        return null
    }

    suspend fun getAllVideo(): List<VideoModel> {
        try {
            val videoResponse = ApiClient.apiService.getAllVideo()
            return videoResponse.videos
        } catch (e: Exception) {
            Log.d("xxx Repository", "getAllVideo Exception: ${e.message}")
        }
        return listOf()
    }

}
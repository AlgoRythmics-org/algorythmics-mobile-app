package com.example.algorythmics.retrofit.repositories

import android.util.Log
import com.example.algorythmics.retrofit.ApiClient
import com.example.algorythmics.retrofit.models.AlgorithmModel
import retrofit2.Response

class AlgorithmRepository {

    suspend fun getOneAlgorithmById(id: String): AlgorithmModel? {
        try{
            return ApiClient.apiService.getOneAlgorithmById(id)
        }catch (e : Exception){
            Log.e("xxx Repository", "getOneAlgorithmById Exception", e)
        }
        return null
    }

    suspend fun getAllAlgorithm(): List<AlgorithmModel> {
        try {
            val algorithmResponse = ApiClient.apiService.getAllAlgorithm()
            return algorithmResponse.algorithms
        } catch (e: Exception) {
            Log.d("xxx Repository", "getAllAlgorithm Exception: ${e.message}")
        }
        return listOf()
    }



}
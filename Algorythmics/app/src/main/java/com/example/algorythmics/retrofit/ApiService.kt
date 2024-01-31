package com.example.algorythmics.retrofit

import com.example.algorythmics.retrofit.models.AlgorithmModel
import retrofit2.http.GET
import retrofit2.http.Path

data class AlgorithmResponse(val algorithms: List<AlgorithmModel>)

interface ApiService {

    @GET("api/algorithm/{id}")
    suspend fun getOneAlgorithmById(@Path("id") id: Long) :AlgorithmModel

    @GET("api/algorithm/allAlgorithms")
    suspend fun getAllAlgorithm(): AlgorithmResponse
}
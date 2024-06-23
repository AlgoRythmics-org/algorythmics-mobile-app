package com.example.algorythmics.retrofit

import com.example.algorythmics.retrofit.models.AlgorithmModel
import com.example.algorythmics.retrofit.models.CodeModel
import com.example.algorythmics.retrofit.models.QuizModel
import com.example.algorythmics.retrofit.models.VideoModel
import retrofit2.http.GET
import retrofit2.http.Path

data class AlgorithmResponse(val algorithms: List<AlgorithmModel>)
data class VideoResponse(val videos: List<VideoModel>)

data class QuizResponse(val quiz: List<QuizModel>)

data class CodeResponse(val code: List<CodeModel>)

interface ApiService {

    @GET("api/algorithm/{id}")
    suspend fun getOneAlgorithmById(@Path("id") id: String) :AlgorithmModel

    @GET("api/algorithm/allAlgorithms")
    suspend fun getAllAlgorithm(): AlgorithmResponse

    @GET("api/video/{id}")
    suspend fun getOneVideoById(@Path("id") id: String) :VideoModel

    @GET("api/video/allVideos")
    suspend fun getAllVideo(): VideoResponse

    @GET("api/quiz/allQuiz")
    suspend fun getAllQuiz() : QuizResponse

    @GET("api/quiz/{algorithmId}")
    fun getQuizByAlgorithmId(@Path("algorithmId") algorithmId : String) :QuizModel

    @GET("api/code/allCode")
    suspend fun getAllCode() : CodeResponse
}
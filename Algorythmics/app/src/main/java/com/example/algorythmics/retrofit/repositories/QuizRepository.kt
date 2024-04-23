package com.example.algorythmics.retrofit.repositories

import android.util.Log
import com.example.algorythmics.retrofit.ApiClient
import com.example.algorythmics.retrofit.models.QuizModel

class QuizRepository {
     fun getQuizByAlgorithmId(algorithmId: String): QuizModel? {
        try{
            return ApiClient.apiService.getQuizByAlgorithmId(algorithmId)
        }catch (e : Exception){
            Log.e("xxx Repository", "getQuizByAlgorithmId Exception", e)
        }
        return null
    }

    suspend fun getAllQuiz(): List<QuizModel> {
        try {
            val quizResponse = ApiClient.apiService.getAllQuiz()
            return quizResponse.quiz
        } catch (e: Exception) {
            Log.d("xxx Repository", "getAllQuiz Exception: ${e.message}")
        }
        return listOf()
    }
}
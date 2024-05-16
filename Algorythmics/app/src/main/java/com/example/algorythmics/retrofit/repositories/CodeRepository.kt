package com.example.algorythmics.retrofit.repositories

import android.util.Log
import com.example.algorythmics.retrofit.ApiClient
import com.example.algorythmics.retrofit.models.CodeModel

class CodeRepository {
    suspend fun getAllCode(): List<CodeModel> {
        try {
            val codeResponse = ApiClient.apiService.getAllCode()
            return codeResponse.code
        } catch (e: Exception) {
            Log.d("xxx Repository", "getAllQuiz Exception: ${e.message}")
        }
        return listOf()
    }
}
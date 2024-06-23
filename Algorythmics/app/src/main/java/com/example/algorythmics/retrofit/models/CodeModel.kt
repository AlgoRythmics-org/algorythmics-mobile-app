package com.example.algorythmics.retrofit.models

import com.google.gson.annotations.SerializedName

data class CodeModel(
    @SerializedName("_id")
    val codeId: String,
    @SerializedName("algorithm_code")
    val algorithmCode: String,
    @SerializedName("algorithm_id")
    val algorithmId: String,
    @SerializedName("answers")
    val answers: String,
    @SerializedName("correct_answers")
    val correctAnswers: String,
)

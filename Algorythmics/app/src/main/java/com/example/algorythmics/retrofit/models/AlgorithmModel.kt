package com.example.algorythmics.retrofit.models

import com.google.gson.annotations.SerializedName

data class AlgorithmModel(
    @SerializedName("_id")
    val algorithmId: String,
    @SerializedName("algorithm_name")
    val algorithmName: String,
    @SerializedName("algorithm_type")
    val algorithmType: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("visualization_type")
    val visualizationType: String,
    @SerializedName("complexity")
    val complexity: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("implementation_type")
    val implementationType: String,
    @SerializedName("learning_steps")
    val learningSteps: String

)

package com.example.algorythmics.providers

import com.example.algorythmics.retrofit.repositories.AlgorithmRepository
import com.example.algorythmics.retrofit.repositories.VideoRepository

object RepositoryProvider {

    val algorithmRepository: AlgorithmRepository = AlgorithmRepository()
    val videoRepository: VideoRepository = VideoRepository()
}
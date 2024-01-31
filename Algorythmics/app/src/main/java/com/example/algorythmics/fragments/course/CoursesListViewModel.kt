package com.example.algorythmics.fragments.course

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.algorythmics.providers.RepositoryProvider
import com.example.algorythmics.retrofit.models.AlgorithmModel

class CoursesListViewModel : ViewModel() {

    private val _algorithmModels = MutableLiveData<List<AlgorithmModel>>()
    val algorithmModels: LiveData<List<AlgorithmModel>> = _algorithmModels

    suspend fun loadAlgorithmData(context: Context) {
        val data = RepositoryProvider.algorithmRepository.getAllAlgorithm()
        _algorithmModels.value = data
    }
}
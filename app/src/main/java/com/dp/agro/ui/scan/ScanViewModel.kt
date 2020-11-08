package com.dp.agro.ui.scan

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dp.agro.data.model.Disease
import com.dp.agro.data.source.AppRepository
import kotlinx.coroutines.CoroutineDispatcher

class ScanViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _disease = MutableLiveData<Disease>()
    val disease: LiveData<Disease>
        get() = _disease

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    fun findDiseaseBySlug(diseaseSlug: String) {
        _isLoading.value = true
        appRepository.findDiseaseBySlug(diseaseSlug) {
            _isLoading.value = false
            _disease.value = it
        }
    }
}
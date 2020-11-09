package com.dp.agro.ui.explore

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dp.agro.data.model.Disease
import com.dp.agro.data.source.AppRepository
import kotlinx.coroutines.CoroutineDispatcher

class ExploreViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _diseases = MutableLiveData<List<Disease>>()
    val diseases: LiveData<List<Disease>>
        get() = _diseases

    fun findAllDiseases() {
        appRepository.findAllDiseases {
            _diseases.value = it
        }
    }
}
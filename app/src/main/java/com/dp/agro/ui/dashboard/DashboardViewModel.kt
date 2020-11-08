package com.dp.agro.ui.dashboard

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dp.agro.data.source.AppRepository
import kotlinx.coroutines.CoroutineDispatcher

class DashboardViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
}
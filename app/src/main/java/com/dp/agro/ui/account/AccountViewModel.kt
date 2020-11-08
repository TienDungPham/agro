package com.dp.agro.ui.account

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.dp.agro.data.source.AppRepository
import kotlinx.coroutines.CoroutineDispatcher

class AccountViewModel @ViewModelInject constructor(
    private val appRepository: AppRepository,
    private val dispatcher: CoroutineDispatcher
) : ViewModel() {
}
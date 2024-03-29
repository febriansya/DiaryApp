package com.bulleh.diaryapp.presentation.screens.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bulleh.diaryapp.data.repository.Diaries
import com.bulleh.diaryapp.data.repository.MongoDB
import com.bulleh.diaryapp.model.RequestState
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result // Update the diaries value with the collected result
            }
        }
    }
}
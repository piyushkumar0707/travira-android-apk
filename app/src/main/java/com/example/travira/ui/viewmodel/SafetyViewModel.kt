package com.example.travira.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travira.data.model.SafetyScore
import com.example.travira.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SafetyViewModel(private val repo: UserRepository = UserRepository()) : ViewModel() {
    private val _safety = MutableStateFlow<SafetyScore?>(null)
    val safety: StateFlow<SafetyScore?> = _safety

    fun load(touristId: String) {
        viewModelScope.launch {
            _safety.value = repo.getSafetyScore(touristId)
        }
    }
}

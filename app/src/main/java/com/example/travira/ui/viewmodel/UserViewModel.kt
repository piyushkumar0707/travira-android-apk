package com.example.travira.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travira.data.model.SafetyScore
import com.example.travira.data.model.Tourist
import com.example.travira.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repo: UserRepository = UserRepository()) : ViewModel() {
    private val _tourist = MutableStateFlow<Tourist?>(null)
    val tourist: StateFlow<Tourist?> = _tourist

    private val _safety = MutableStateFlow<SafetyScore?>(null)
    val safety: StateFlow<SafetyScore?> = _safety

    fun setTourist(t: Tourist) {
        _tourist.value = t
    }

    fun fetchSafetyScore() {
        viewModelScope.launch {
            _tourist.value?.let {
                _safety.value = repo.getSafetyScore(it.id)
            }
        }
    }
}

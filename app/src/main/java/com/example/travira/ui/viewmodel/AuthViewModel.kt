package com.example.travira.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travira.data.model.Tourist
import com.example.travira.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: UserRepository = UserRepository()) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tourist = MutableStateFlow<Tourist?>(null)
    val tourist: StateFlow<Tourist?> = _tourist

    fun register(name: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val t = repo.registerTourist(name)
                _tourist.value = t
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}

package com.example.travira.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlertViewModel : ViewModel() {
    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean> = _sending

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result

    fun sendPanic(touristId: String?, lat: Double?, lon: Double?) {
        viewModelScope.launch {
            _sending.value = true
            delay(500) // simulate network
            _sending.value = false
            _result.value = "Panic sent (dummy)."
        }
    }
}

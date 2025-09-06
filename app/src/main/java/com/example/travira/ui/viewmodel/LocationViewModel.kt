package com.example.travira.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travira.data.model.UserLocation
import com.example.travira.data.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {
    private val _location = MutableStateFlow<UserLocation?>(null)
    val location: StateFlow<UserLocation?> = _location

    fun startLocationUpdates() {
        viewModelScope.launch {
            repository.getLocationUpdates().collect { pair ->
                _location.value = UserLocation(pair.first, pair.second)
            }
        }
    }
}

class LocationViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = LocationRepository(context.applicationContext)
        @Suppress("UNCHECKED_CAST")
        return LocationViewModel(repo) as T
    }
}

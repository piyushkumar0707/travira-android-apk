package com.example.travira.data.repository

import android.content.Context
import com.example.travira.utils.LocationUtils
import kotlinx.coroutines.flow.Flow

class LocationRepository(context: Context) {
    private val locationUtils = LocationUtils(context)

    fun getLocationUpdates(): Flow<Pair<Double, Double>> {
        return locationUtils.getLocationUpdates()
    }
}

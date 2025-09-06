package com.example.travira.utils

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationUtils(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    // Stream of location updates
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Pair<Double, Double>> = callbackFlow {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000L // every 2 seconds
        ).build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation
                if (loc != null) {
                    trySend(Pair(loc.latitude, loc.longitude))
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(request, callback, null)
        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }
}

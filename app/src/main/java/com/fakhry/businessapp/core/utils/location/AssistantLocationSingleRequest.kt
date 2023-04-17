package com.fakhry.businessapp.core.utils.location

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnFailureListener
import timber.log.Timber

class AssistantLocationSingleRequest(
    private val context: Context,
    private val listener: AssistantLocationListener,
) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var locationRequest: LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,  (10 * 1000).toLong())// 10 seconds
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis((5 * 1000).toLong()) // 5 seconds
            .setMaxUpdateDelayMillis((20 * 1000).toLong())
            .build()


    private val locationCallback: LocationCallback
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    fun requestLocation() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation()
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((context as Activity)) { getLocation() }
                .addOnFailureListener(context, OnFailureListener { e: Exception ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(context, GPS_REQ)
                            } catch (sie: SendIntentException) {
                                Timber.i(e.message)
                                val errMessage = "PendingIntent unable to execute request."
                                listener.onFailedGetLocation(errMessage)
                            }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errMessage =
                                "Location settings are inadequate, and cannot be fixed here. Fix in Settings."
                            listener.onFailedGetLocation(errMessage)
                        }
                    }
                })
        }
    }

    fun onDetach() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                listener.onSuccessGetLocation(location)
            } else {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }
        }
    }

    init {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)
        mLocationSettingsRequest = builder.build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        listener.onSuccessGetLocation(location)
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
            }
        }
    }

    companion object {
        const val GPS_REQ = 99
    }
}
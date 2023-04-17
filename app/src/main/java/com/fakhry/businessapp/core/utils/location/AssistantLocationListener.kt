package com.fakhry.businessapp.core.utils.location

import android.location.Location

interface AssistantLocationListener {
    fun onSuccessGetLocation(location: Location)
    fun onFailedGetLocation(errorMessage: String)
}
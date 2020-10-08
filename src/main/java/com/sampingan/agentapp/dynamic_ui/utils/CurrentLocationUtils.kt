/*
 * Copyright (c) 2020. 
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.provider.Settings
import com.google.android.gms.location.*
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.dialog.ErrorStateDialog
import com.sampingan.agentapp.dynamic_ui.extension.isActiveGPS

/**
 * Created by ilgaputra15
 * on Tuesday, 28/04/2020 23.11
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

class CurrentLocationUtils(val activity: Activity) {

    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val errorStateDialog = ErrorStateDialog(activity) {}

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 0
    }

    fun getCurrentLocation(locationCallBack: (Location?) -> Unit) {
        if ((!Permission.checkPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_LOCATION_PERMISSION
            )) && (!Permission.checkPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                REQUEST_LOCATION_PERMISSION
            ))
        ) {
            locationCallBack.invoke(null)
            return
        }

        if (!activity.isActiveGPS()) {
            showDialog()
            locationCallBack.invoke(null)
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        locationRequest = LocationRequest.create()
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest?.interval = 20 * 1000

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    locationCallBack.invoke(null)
                } else {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            locationCallBack.invoke(location)
                            break
                        }
                    }
                }
                fusedLocationClient?.removeLocationUpdates(locationCallback)
            }
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
            if (location != null) locationCallBack.invoke(location)
            else fusedLocationClient?.requestLocationUpdates(locationRequest,
                locationCallback,
                null)
        }
    }

    private fun showDialog() {
        errorStateDialog.permissionDeniedDialog(
            Manifest.permission.ACCESS_FINE_LOCATION,
            imageDrawable = R.drawable.ic_empty_location
        ) {
            navigationToGPSSettings()
        }
    }

    private fun navigationToGPSSettings() {
        activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

}
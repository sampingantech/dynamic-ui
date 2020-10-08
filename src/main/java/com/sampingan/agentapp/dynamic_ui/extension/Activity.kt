/*
 * Copyright (c) 2020. 
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.extension

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by ilgaputra15
 * on Saturday, 28/03/2020 12.21
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

fun AppCompatActivity.hideSoftKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}

fun Activity.isActiveGPS(): Boolean {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}
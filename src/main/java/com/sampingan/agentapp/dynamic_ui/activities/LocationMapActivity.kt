/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.model.Location
import com.sampingan.agentapp.dynamic_ui.utils.Permission
import kotlinx.android.synthetic.main.activity_location_map.*
import java.io.IOException
import java.util.*

/**
 * Created by ilgaputra15
 * on Tuesday, 24/03/2020 09.46
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class LocationMapActivity :
    AppCompatActivity(),
    OnMapReadyCallback,
    View.OnClickListener,
    GoogleMap.OnCameraIdleListener {

    companion object {
        const val REQUEST_PERMISSION_MAP = 1
        const val IS_FIX_LOCATION = "fixLocation"
        const val KEY = "key"
        fun generateIntent(
            context: Context,
            key: String = "",
            isFixLocation: Boolean = false
        ): Intent {
            val intent = Intent(context, LocationMapActivity::class.java)
            intent.putExtra(IS_FIX_LOCATION, isFixLocation)
            intent.putExtra(KEY, key)
            return intent
        }
    }

    private var address = ""
    private var latitude = 0.0
    private var longitude = 0.0
    private var isSelected = false
    private var key = ""

    private var isFixLocation = false

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_map)
        setup()
    }

    private fun setup() {
        isFixLocation = intent.getBooleanExtra(IS_FIX_LOCATION, false)
        key = intent.getStringExtra(KEY) ?: ""
        initialMapSDK()
        buttonBack.setOnClickListener(this)
        buttonSubmitAddress.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            buttonSubmitAddress -> sendBackValue()
            buttonBack -> finish()
        }
    }

    private fun initialMapSDK() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.frameMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mGoogleMap: GoogleMap) {
        if (!isLocationAllowed()) return
        googleMap = mGoogleMap
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        googleMap.uiSettings.isZoomControlsEnabled = !isFixLocation
        googleMap.uiSettings.isZoomGesturesEnabled = !isFixLocation
        googleMap.uiSettings.isScrollGesturesEnabled = !isFixLocation
        googleMap.uiSettings.isRotateGesturesEnabled = !isFixLocation
        googleMap.uiSettings.isTiltGesturesEnabled = !isFixLocation
        googleMap.isMyLocationEnabled = !isFixLocation
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        googleMap.setOnCameraIdleListener(this)
        setLocationMap()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_MAP -> {
                if (grantResults.isNotEmpty() &&
                    (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    setLocationMap()
                } else {
                    Toast.makeText(
                        this,
                        "You don't have permission to access your location",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
                return
            }
        }
    }

    private fun dialogErrorGPS() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("GPS is Off")
        builder.setMessage("Please Turn on your GPS")
        builder.setPositiveButton("Setting") { _, _ ->
            navigationToGPSSettings()
            Toast.makeText(
                applicationContext,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton(getString(R.string.text_ok)) { _, _ ->
            Toast.makeText(
                applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT
            ).show()
        }
        builder.show()
    }


    private fun navigationToGPSSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    private fun checkStatusGPS() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!gpsStatus) dialogErrorGPS()
    }

    private fun isLocationAllowed(): Boolean {
        return Permission.checkPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            REQUEST_PERMISSION_MAP
        )
    }

    @SuppressLint("MissingPermission")
    fun setLocationMap() {
        if (!isLocationAllowed()) return
        checkStatusGPS()
        googleMap.setOnMyLocationButtonClickListener {
            setLocationMap()
            false
        }
        if (isSelected) {
            val currentLatLng = LatLng(latitude, longitude)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            inputLocation.text = address
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
                }
            }
        }

    }

    override fun onCameraIdle() {
        val pinPosition = googleMap.cameraPosition.target
        latitude = pinPosition.latitude
        longitude = pinPosition.longitude
        address = getAddress(this, pinPosition.latitude, pinPosition.longitude)
        inputLocation.text = address
        buttonSubmitAddress.isEnabled = address.isNotEmpty()
    }

    private fun getAddress(context: Context, lat: Double, lng: Double): String {
        var addressLine = ""
        try {
            val geoCoder = Geocoder(context, Locale.getDefault())
            val addresses = geoCoder.getFromLocation(lat, lng, 1)
            if (addresses.size > 0) {
                val valueAddress = addresses[0]
                val cityAddress = valueAddress.getAddressLine(0)
                if (!cityAddress.isNullOrEmpty()) addressLine = cityAddress
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return addressLine
    }


    private fun sendBackValue() {
        if (address.isEmpty() and (latitude == 0.0) and (longitude == 0.0)) {
            return
        }
        val location = Location(key = key, addressName = address, lat = latitude, long = longitude)
        val intent = Intent()
        intent.putExtra(Location.LOCATION_DATA, location)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

package com.sampingan.agentapp.dynamic_ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ilgaputra15
 * on Tuesday, 24/03/2020 10.41
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

@Parcelize
data class Location(
    var addressName: String,
    var lat: Double,
    var long: Double,
    var key: String = ""
) : Parcelable {
    companion object {
        const val LOCATION_DATA = "locationData"
        const val GET_LOCATION = 1
    }
}
/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.utils

import android.util.TypedValue
import android.view.View
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlin.math.roundToInt

/**
 * Created by ilgaputra15
 * on Saturday, 28/03/2020 19.51
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
object Converts {
    private val gson = Gson()
    fun convertMapToJsonObject(data: Map<String, Any>): JsonObject {
        return JsonParser().parse(gson.toJson(data).toString()).asJsonObject
    }

    fun View.dpToPx(dp: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
            .roundToInt()
}
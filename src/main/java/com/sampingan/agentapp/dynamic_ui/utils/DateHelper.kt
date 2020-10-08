package com.sampingan.agentapp.dynamic_ui.utils

import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.IN_LANGUANGE
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.RCF3666_DATE_PATTERN
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ilgaputra15
 * on Saturday, 21/03/2020 21.02
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
internal object DateHelper {

    fun ddMMMMyyyyFormat(valueDate: String): String {
        val responseFormat = SimpleDateFormat(RCF3666_DATE_PATTERN, Locale(IN_LANGUANGE))
        val showFormat = SimpleDateFormat("dd MMMM yyyy", Locale(IN_LANGUANGE))
        val dateTimeOfValueDate: Date = responseFormat.parse(valueDate)!!
        return showFormat.format(dateTimeOfValueDate)
    }

    fun timeSimpleFormat(valueDate: String): String {
        val responseFormat = SimpleDateFormat(RCF3666_DATE_PATTERN, Locale(IN_LANGUANGE))
        val showFormat = SimpleDateFormat("hh.mm a", Locale(IN_LANGUANGE))
        val dateTimeOfValueDate: Date = responseFormat.parse(valueDate)!!
        return showFormat.format(dateTimeOfValueDate)
    }

    fun convertDateToString(value: Date, pattern: String): String {
        val formatter: DateFormat = SimpleDateFormat(pattern, Locale(IN_LANGUANGE))
        return formatter.format(value)
    }
}
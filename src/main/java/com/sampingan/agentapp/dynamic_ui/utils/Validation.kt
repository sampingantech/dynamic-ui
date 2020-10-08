package com.sampingan.agentapp.dynamic_ui.utils

import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.CHECKBOX_WIDGET
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.EMAIL_WIDGET
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.RADIO_WIDGET
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.SELECT_WIDGET
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.BASE64_PATTERN
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.COLON_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.EMAIL_REGEX_VALIDATION
import java.util.regex.Pattern

/**
 * Created by ilgaputra15
 * on Saturday, 28/03/2020 21.24
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

object Validation {
    fun checkValidate(widget: String?, value: String): Boolean {
        return when (widget) {
            EMAIL_WIDGET -> isEmailValid(value)
            SELECT_WIDGET -> isLogicJumpSelected(value)
            RADIO_WIDGET -> isLogicJumpSelected(value)
            CHECKBOX_WIDGET -> isOtherValueNotEmpty(value)
            null -> false
            else -> true
        }
    }

    fun isStringBase64(data: String): Boolean {
        return data.contains("$BASE64_PATTERN$COLON_TEXT")
    }

    private fun isLogicJumpSelected(value: String?): Boolean {
        val extractValue = value?.split(";").orEmpty()
        val enumValue = extractValue[0]
        val enumSection = extractValue[1]

        return ((enumValue == "-" || enumValue == "null" || enumValue.isBlank()) && enumSection == "null").not()
    }

    private fun isEmailValid(email: String): Boolean {
        val pattern = Pattern.compile(EMAIL_REGEX_VALIDATION, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            return false
        }
        return true
    }

    private fun isOtherValueNotEmpty(value: String): Boolean {
        return value.isNotEmpty()
    }
}
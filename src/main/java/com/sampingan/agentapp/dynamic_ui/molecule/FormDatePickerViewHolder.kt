package com.sampingan.agentapp.dynamic_ui.molecule

import android.view.View
import androidx.core.content.ContextCompat
import com.sampingan.agentapp.dynamic_ui.utils.DateHelper
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.DATE_WIDGET
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.TIME_WIDGET
import com.sampingan.agentapp.dynamic_ui.dialog.DateTimePickerDialog
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.rule.toDescription
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DATE_PATTERN
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DATE_TIME_PATTERN
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.FULL_DATE_PATTERN
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.STAR_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.TIME_PATTERN
import kotlinx.android.synthetic.main.molecule_form_check_box.view.textTitle
import kotlinx.android.synthetic.main.molecule_form_date_picker.view.*
import java.util.*


/**
 * Created by ilgaputra15
 * on Sunday, 22/03/2020 16.04
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormDatePickerViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_date_picker
    }

    private val calender = Calendar.getInstance()

    fun bind(data: DynamicView) {
        val title =
            if (data.isRequired) "${data.jsonSchema.title} $STAR_TEXT" else data.jsonSchema.title
        itemView.textTitle.text = title
        val picker =
            DateTimePickerDialog(itemView.context)
        itemView.frameAction.setOnClickListener {
            when (data.uiSchemaRule.uiWidget) {
                DATE_WIDGET -> {
                    picker.setValueOfDate(calender) {
                        setValue(data, DATE_PATTERN)
                    }
                }
                TIME_WIDGET -> {
                    picker.setValueOfTime(calender) {
                        setValue(data, TIME_PATTERN)
                    }
                }
                else -> {
                    picker.setValueOfDate(calender) {
                        picker.setValueOfTime(calender) {
                            setValue(data, DATE_TIME_PATTERN)
                        }
                    }
                }
            }
        }
        if (data.preview != null) itemView.textValue.text = data.preview as String
        itemView.frameAction.isEnabled = data.isEnable
        setError(data)
        itemView.textDesc.let {
            it.visible = data.uiSchemaRule.toDescription(data.jsonSchema) != null
            it.text = "${data.uiSchemaRule.toDescription(data.jsonSchema)}"
        }
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
    }

    private fun setError(data: DynamicView) {
        val color = if (data.isError) R.color.reddish else R.color.very_light_grey
        itemView.viewLine.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
        if (data.isError && data.value == null)
            itemView.textError.let {
                it.text =
                    itemView.context.getString(R.string.text_can_not_empty, data.jsonSchema.title)
                it.visible = data.isError
            }
        else if (data.isError && data.value != null)
            itemView.textError.let {
                it.text = data.errorValue
                it.visible = data.errorValue != null
            }
        else itemView.textError.visible = false
    }

    fun setValue(data: DynamicView, pattern: String) {
        itemView.textValue.requestFocus()
        val preview = DateHelper.convertDateToString(calender.time, pattern)
        itemView.textValue.text = preview
        data.preview = preview
        data.value = DateHelper.convertDateToString(calender.time, FULL_DATE_PATTERN)
    }
}
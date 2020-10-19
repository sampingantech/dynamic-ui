package co.sampingan.android.dynamic_ui.molecule

import android.view.View
import androidx.core.content.ContextCompat
import co.sampingan.android.dynamic_ui.utils.DateHelper
import co.sampingan.android.dynamic_ui.model.DynamicView
import co.sampingan.android.dynamic_ui.R
import co.sampingan.android.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.DATE_WIDGET
import co.sampingan.android.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.TIME_WIDGET
import co.sampingan.android.dynamic_ui.dialog.DateTimePickerDialog
import co.sampingan.android.dynamic_ui.extension.visible
import co.sampingan.android.dynamic_ui.rule.toDescription
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.DATE_PATTERN
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.DATE_TIME_PATTERN
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.FULL_DATE_PATTERN
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.STAR_TEXT
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.TIME_PATTERN
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
                        setValue(data, getPattern(data.uiSchemaRule.uiWidget))
                    }
                }
                TIME_WIDGET -> {
                    picker.setValueOfTime(calender) {
                        setValue(data, getPattern(data.uiSchemaRule.uiWidget))
                    }
                }
                else -> {
                    picker.setValueOfDate(calender) {
                        picker.setValueOfTime(calender) {
                            setValue(data, getPattern(data.uiSchemaRule.uiWidget))
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

    private fun getPattern(uiWidget: String?): String {
        return when (uiWidget) {
            DATE_WIDGET -> DATE_PATTERN
            TIME_WIDGET -> TIME_PATTERN
            else -> DATE_TIME_PATTERN
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
        else if (data.isError && data.value != null) {
            itemView.textError.let {
                it.text = data.errorValue
                it.visible = data.errorValue != null
            }
            itemView.textValue.text =
                DateHelper.convertDateToString(
                    calender.time,
                    getPattern(data.uiSchemaRule.uiWidget)
                )

        } else itemView.textError.visible = false
    }

    fun setValue(data: DynamicView, pattern: String) {
        itemView.textValue.requestFocus()
        val preview = DateHelper.convertDateToString(calender.time, pattern)
        itemView.textValue.text = preview
        data.preview = preview
        data.value = DateHelper.convertDateToString(calender.time, FULL_DATE_PATTERN)
    }
}
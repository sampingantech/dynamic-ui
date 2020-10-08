package com.sampingan.agentapp.dynamic_ui.molecule

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.EMAIL_WIDGET
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.PHONE_WIDGET
import com.sampingan.agentapp.dynamic_ui.adapter.TaskSubmissionAdapter.Companion.PRICE_WIDGET
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.utils.Validation
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.rule.toDescription
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.INTEGER_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.PHONE_PREFIX
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.PRICE_PREFIX
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.STAR_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Validation.checkValidate
import kotlinx.android.synthetic.main.molecule_form_edit_text.view.*

/**
 * Created by ilgaputra15
 * on Sunday, 22/03/2020 16.04
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormEditTextViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_edit_text
    }

    fun bind(data: DynamicView) {
        val title =
            if (data.isRequired) "${data.jsonSchema.title} $STAR_TEXT" else data.jsonSchema.title
        itemView.textTitle.text = title
        val inputType = when (data.jsonSchema.type) {
            INTEGER_TEXT -> InputType.TYPE_CLASS_NUMBER
            else -> InputType.TYPE_CLASS_TEXT
        }

        val prefix = when (data.uiSchemaRule.uiWidget) {
            PHONE_WIDGET -> PHONE_PREFIX
            PRICE_WIDGET -> PRICE_PREFIX
            else -> ""
        }
        itemView.textPrefix.let {
            it.text = prefix
            it.visible = prefix.isNotEmpty()
        }
        if (data.preview != null) itemView.editTextValue.setText(data.preview.toString())
        itemView.editTextValue.isEnabled = data.isEnable
        itemView.editTextValue.inputType = inputType
        itemView.editTextValue.isSingleLine = false

        itemView.editTextValue.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(string: Editable?) {
                if (itemView.editTextValue.text.hashCode() == string.hashCode()) {
                    var value: String? = itemView.editTextValue.text.toString()
                    value = if (value.isNullOrEmpty()) null else value.toString().trim()
                    val isValid =
                        checkValidate(data.uiSchemaRule.uiWidget.orEmpty(), value.orEmpty())
                    data.value = value
                    data.preview = value
                    data.isValidated = isValid
                    if (!isValid) data.errorValue =
                        itemView.context.getString(R.string.wrong_format, data.jsonSchema.title)
                }
            }

            override fun beforeTextChanged(string: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
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
}
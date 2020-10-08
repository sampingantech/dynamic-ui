package com.sampingan.agentapp.dynamic_ui.molecule

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.model.RadioValue
import com.sampingan.agentapp.dynamic_ui.rule.toDescription
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.BOOLEAN_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DEFAULT_NO_VALUE
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DEFAULT_OTHER_VALUE
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DEFAULT_YES_VALUE
import kotlinx.android.synthetic.main.molecule_form_radio.view.*


/**
 * Created by ilgaputra15
 * on Sunday, 22/03/2020 16.04
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormRadioViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_radio
    }

    var name = ""
    var dataPreviewSet = false
    private val params = RadioGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    fun bind(data: DynamicView, itemClickListener: (widget: String, key: String?) -> Unit) {
        dataPreviewSet = false
        var id = 0
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        val values = data.jsonSchema.enum ?: listOf(DEFAULT_YES_VALUE, DEFAULT_NO_VALUE)
        val labels = data.jsonSchema.enumNames ?: values
        val jumpLogic = data.jsonSchema.enumSectionTargetRef
        val value = mutableListOf<RadioValue>()

        value.addAll(
            values.mapIndexed { index, element ->
                RadioValue(
                    label = labels[index] ?: element ?: DEFAULT_OTHER_VALUE,
                    value = element,
                    sectionTargetRef = jumpLogic?.get(index)
                )
            }
        )
        itemView.radioGroup.removeAllViews()
        value.forEach { radio ->
            if (radio.value != null) {
                val radioButton = generateRadioButton(radio, id++)
                radioButton.setOnClickListener {
                    name = radio.value
                    data.enumSectionTargetRef = radio.sectionTargetRef
                    if (data.jsonSchema.type == BOOLEAN_TEXT) {
                        data.value = name == DEFAULT_YES_VALUE
                        data.preview = name == DEFAULT_YES_VALUE
                    } else {
                        data.value = name
                        data.preview = radio.label
                    }
                    itemView.customValueRadioButton.isChecked = false
                    itemClickListener(data.uiSchemaRule.uiWidget!!, data.enumSectionTargetRef)
                }
                @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                if (data.isError)
                    radioButton.buttonTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.reddish)
                    )
                if (data.preview != null) {
                    val preview = when (values) {
                        null -> if (data.preview == true) DEFAULT_YES_VALUE else DEFAULT_NO_VALUE
                        else -> data.preview.toString()
                    }
                    if (preview == radio.label) {
                        radioButton.isChecked = true
                        dataPreviewSet = true
                    }
                }
                radioButton.isEnabled = data.isEnable
                itemView.radioGroup.addView(radioButton)
            } else {
                generateRadioCustomValue(radio, data.preview) {
                    data.value = it
                    data.preview = it
                    if (it.isEmpty() || it.isBlank()) {
                        data.isValidated = false
                        data.errorValue = itemView.context.getString(
                            R.string.text_can_not_empty,
                            itemView.context.getString(R.string.text_other)
                        )
                    } else data.isValidated = true

                }
            }
        }
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

    private fun generateRadioCustomValue(
        value: RadioValue,
        preview: Any?,
        actionClick: ((String) -> Unit),
    ) = itemView.apply {
        customValueContainer.visible = true
        customValueRadioButton.isChecked = name == value.tempValue
        if (preview != null && dataPreviewSet.not()) {
            customValueRadioButton.performClick()
            customValueEditText.setText(preview.toString())
        }
        customValueContainer.setOnClickListener {
            customValueRadioButton.performClick()
        }
        customValueEditText.apply {
            addTextChangedListener { text ->
                text.toString().let {
                    value.tempValue = it
                    actionClick(it)
                }
            }
            setOnClickListener {
                customValueRadioButton.performClick()
            }
        }
        customValueRadioButton.setOnCheckedChangeListener { _, b ->
            if (b) {
                itemView.radioGroup.clearCheck()
                name = customValueEditText.text.toString()
                actionClick(name)

            }
            customValueEditText.isEnabled = b
        }
    }

    private fun generateRadioButton(value: RadioValue, id: Int): RadioButton {
        params.setMargins(0, 24, 0, 0)
        val button = RadioButton(itemView.context)
        button.setPadding(36, 0, 0, 0)
        button.textSize = 12F
        button.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_grey))
        button.id = id
        button.layoutParams = params
        button.text = value.label
        button.isChecked = name == value.value
        return button
    }

    private fun setError(data: DynamicView) {
        val color =
            if (data.isError && data.value == null) R.color.reddish else R.color.very_light_grey
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
        else if (data.isValidated.not())
            itemView.textError.let {
                it.text = data.errorValue
                it.visible = data.errorValue != null
            }
        else itemView.textError.visible = false
    }
}
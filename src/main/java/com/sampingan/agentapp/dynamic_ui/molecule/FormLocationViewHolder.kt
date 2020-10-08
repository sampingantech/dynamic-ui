package com.sampingan.agentapp.dynamic_ui.molecule

import android.view.View
import androidx.core.content.ContextCompat
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.model.Location
import com.sampingan.agentapp.dynamic_ui.rule.toDescription
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.STAR_TEXT
import kotlinx.android.synthetic.main.molecule_form_location.view.*

/**
 * Created by ilgaputra15
 * on Sunday, 22/03/2020 16.04
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormLocationViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_location
    }

    fun bind(data: DynamicView, itemClickListener: (widget: String, key: String) -> Unit) {
        val title =
            if (data.isRequired) "${data.jsonSchema.title} $STAR_TEXT" else data.jsonSchema.title
        itemView.textTitle.text = title
        itemView.frameLocation.setOnClickListener {
            itemClickListener.invoke(data.uiSchemaRule.uiWidget!!, data.componentName)
        }

        if (data.preview != null) {
            when (data.preview) {
                is Location -> {
                    val result = data.preview as Location
                    data.value = "${result.lat},${result.long}"
                    itemView.textValue.text = result.addressName
                }
                else -> itemView.textValue.text = data.preview.toString()
            }
        }

        itemView.frameLocation.isEnabled = data.isEnable
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
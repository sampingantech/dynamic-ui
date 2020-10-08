/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.molecule

import android.view.View
import coil.api.load
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.rule.toDescription
import com.sampingan.agentapp.dynamic_ui.rule.toTitle
import io.noties.markwon.*
import kotlinx.android.synthetic.main.molecule_form_description.view.*


class FormDescriptionViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_description
    }

    private lateinit var markwon: Markwon

    fun bind(data: DynamicView, itemClickListener: (widget: String, key: String) -> Unit) {
        markwon = buildMarkwon(
            context = itemView.context,
            itemClickListener = itemClickListener,
            uiWidget = data.uiSchemaRule.uiWidget!!
        )

        val title = data.uiSchemaRule.toTitle(data.jsonSchema)
        itemView.textTitle.visible = title.isNotBlank()
        markwon.setMarkdown(itemView.textTitle, title)

        val description = data.uiSchemaRule.toDescription(data.jsonSchema) ?: ""
        itemView.textDescription.visible = description.isNotBlank()
        markwon.setMarkdown(itemView.textDescription, description)
    }
}
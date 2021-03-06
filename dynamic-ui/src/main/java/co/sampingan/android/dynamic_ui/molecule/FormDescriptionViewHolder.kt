/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package co.sampingan.android.dynamic_ui.molecule

import android.view.View
import co.sampingan.android.dynamic_ui.model.DynamicView
import co.sampingan.android.dynamic_ui.R
import co.sampingan.android.dynamic_ui.extension.visible
import co.sampingan.android.dynamic_ui.rule.toDescription
import co.sampingan.android.dynamic_ui.rule.toTitle
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
package com.sampingan.agentapp.dynamic_ui.molecule

import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.adapter.DropDownRecyclerAdapter
import com.sampingan.agentapp.dynamic_ui.extension.afterTextChanged
import com.sampingan.agentapp.dynamic_ui.extension.fullExpanded
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.model.DropDownValue
import com.sampingan.agentapp.dynamic_ui.rule.toDescription
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DEFAULT_OTHER_VALUE
import kotlinx.android.synthetic.main.dialog_drop_down.view.*
import kotlinx.android.synthetic.main.molecule_form_drop_down.view.*
import java.util.*


/**
 * Created by ilgaputra15
 * on Sunday, 22/03/2020 16.04
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormDropDownViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_drop_down
        const val LIMIT_SHOW_SEARCH = 15
    }

    fun bind(data: DynamicView, itemClickListener: (widget: String, key: String?) -> Unit) {
        val title = if (data.isRequired) "${data.jsonSchema.title} *" else data.jsonSchema.title
        itemView.textTitle.text = title
        val placeholder = data.uiSchemaRule.uiPlaceholder ?: data.jsonSchema.title

        val value = data.jsonSchema.enum ?: listOf(DEFAULT_OTHER_VALUE)
        val label = data.jsonSchema.enumNames ?: value
        val target = data.jsonSchema.enumSectionTargetRef ?: value

        val values = value.mapIndexed { index, element ->
            DropDownValue(
                label = label[index] ?: DEFAULT_OTHER_VALUE,
                value = element,
                sectionTargetRef = target[index]
            )
        }

        showSelectedValue(placeholder, values
            .find { data.preview != null && (it.value == data.preview || it.label == data.preview) }
            ?.apply { isSelected = true }
        )

        itemView.frameValue.setOnClickListener {
            dropDownDialog(placeholder, values) { selected ->
                data.value = selected?.value ?: selected?.tempValue ?: selected?.label
                data.preview = selected?.label
                data.enumSectionTargetRef = selected?.sectionTargetRef
                showSelectedValue(placeholder, selected)
                itemClickListener(data.uiSchemaRule.uiWidget!!, data.enumSectionTargetRef)
            }
        }
        itemView.textDesc.let {
            it.visible = data.uiSchemaRule.toDescription(data.jsonSchema) != null
            it.text = "${data.uiSchemaRule.toDescription(data.jsonSchema)}"
        }
        itemView.buttonHelp.let {
            it.visible = data.uiSchemaRule.uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
        setError(data)
    }

    private fun showSelectedValue(placeholder: String?, selected: DropDownValue?) {
        itemView.textValue.text = selected?.label ?: placeholder
    }

    private fun dropDownDialog(
        title: String,
        list: List<DropDownValue>,
        selectedItem: (DropDownValue?) -> Unit,
    ) {
        val bottomDialog = BottomSheetDialog(itemView.context, R.style.DialogWithKeyboardStyle)
        val layout = View.inflate(itemView.context, R.layout.dialog_drop_down, null)
        bottomDialog.setContentView(layout)
        bottomDialog.fullExpanded(layout)
        var clickedItem: DropDownValue? = null
        val adapter = DropDownRecyclerAdapter(list,
            clickListener = { clickedItem = it })
            .apply {
                setHasStableIds(true)
            }
        with(layout) {
            editTextSearch.visible = list.size > LIMIT_SHOW_SEARCH
            textDialogTitle.text = title
            recyclerView.adapter = adapter
            imageClose.setOnClickListener { bottomDialog.dismiss() }
            buttonApply.setOnClickListener {
                selectedItem.invoke(clickedItem)
                bottomDialog.dismiss()
            }
            editTextSearch.afterTextChanged { query ->
                val filter = list.filter {
                    it.label.toLowerCase(Locale.getDefault())
                        .contains(query.toString().toLowerCase(Locale.getDefault()))
                }
                adapter.showDataFilter(filter)
            }
        }
        bottomDialog.show()
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
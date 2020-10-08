package co.sampingan.android.dynamic_ui.molecule

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableRow
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.sampingan.agentapp.dynamic_ui.R
import co.sampingan.android.dynamic_ui.extension.visible
import co.sampingan.android.dynamic_ui.model.DynamicView
import co.sampingan.android.dynamic_ui.rule.toDescription
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.ENUM_KEY
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.STAR_TEXT
import co.sampingan.android.dynamic_ui.utils.Validation
import kotlinx.android.synthetic.main.molecule_form_check_box.view.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.forEachIndexed
import kotlin.collections.isNotEmpty
import kotlin.collections.set


/**
 * Created by ilgaputra15
 * on Thursday, 19/03/2020 22.00
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormCheckBoxViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_check_box
    }

    private val datas = HashMap<Int, String>()

    fun bind(data: DynamicView) {
        setupTitle(data.isRequired, data.jsonSchema.title)
        setupDescription(data.uiSchemaRule.toDescription(data.jsonSchema))
        setupPreview(data.preview)
        setupButtonHelp(data.uiSchemaRule.uiHelp, data)
        itemView.linearLayoutCheckBox.removeAllViews()
        val value = data.jsonSchema.items?.get(ENUM_KEY)?.asJsonArray
        value?.forEachIndexed { index, jsonElement ->
            if (jsonElement.isJsonNull) {
                val checkBoxOther = generateCheckBoxOther(
                        index = index,
                        context = itemView.context,
                        isError = data.isError,
                        isEnable = data.isEnable
                ) { value, isChecked ->
                    setValue(value, isChecked, index)
                    val isNotValid = isChecked && Validation.checkValidate(
                            widget = data.uiSchemaRule.uiWidget.orEmpty(),
                            value = value
                    ).not()
                    data.value = convertHashMapToList(datas)
                    data.preview = datas
                    data.isValidated = isNotValid.not()
                    val otherString = itemView.context.getString(R.string.text_other)
                    if (isNotValid) data.errorValue = itemView.context.getString(R.string.text_can_not_empty, otherString)
                }
                itemView.linearLayoutCheckBox.addView(checkBoxOther)
            } else {
                val checkBox = generateCheckBox(jsonElement.asString, index)
                if (data.isError)
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        checkBox.buttonTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(itemView.context, R.color.reddish)
                        )
                    }
                checkBox.setOnClickListener {
                    setValue(jsonElement.asString, checkBox.isChecked, checkBox.id)
                    data.value = convertHashMapToList(datas)
                    data.preview = datas
                }
                checkBox.isEnabled = data.isEnable
                itemView.linearLayoutCheckBox.addView(checkBox)
            }
        }
        setError(data)
    }

    private fun setupTitle(isRequired: Boolean, title: String) {
        itemView.textTitle.text = if (isRequired) "$title $STAR_TEXT" else title
    }

    private fun setupDescription(desc: String?) {
        itemView.textDesc.let {
            it.visible = desc.isNullOrEmpty().not()
            it.text = desc
        }
    }

    private fun setupPreview(preview: Any?) {
        if (preview != null) {
            datas.putAll(preview as Map<Int, String>)
        }
    }

    private fun setupButtonHelp(uiHelp: Any?, data: DynamicView) {
        itemView.buttonHelp.let {
            it.visible = uiHelp != null
            it.setOnClickListener { criteriaDialog(data) }
        }
    }

    private fun generateCheckBox(value: String, index: Int): CheckBox {
        val params = TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 24, 0, 0)
        val checkBox = CheckBox(itemView.context)
        checkBox.layoutParams = params
        checkBox.id = index
        checkBox.setPadding(36, 0, 0, 0)
        checkBox.textSize = 12F
        checkBox.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown_grey))
        checkBox.text = value
        checkBox.isChecked = datas.containsKey(index)
        return checkBox
    }

    private fun generateCheckBoxOther(
            index: Int,
            context: Context,
            isError: Boolean,
            isEnable: Boolean,
            actionClick: (String, Boolean) -> Unit
    ): LinearLayout {
        val paramsContainer = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 24, 0, 0) }
        val checkBox = generateCheckBox(context, isEnable, isError, index)
        val editText = generateEditText(context, index).apply {
            isEnabled = checkBox.isChecked
            setOnClickListener { checkBox.performClick() }
            addTextChangedListener { text -> actionClick(text.toString(), checkBox.isChecked) }
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            actionClick(editText.text.toString(), isChecked)
            editText.isEnabled = isChecked
        }
        return LinearLayout(context).apply {
            layoutParams = paramsContainer
            orientation = LinearLayout.HORIZONTAL
            isClickable = true
            isFocusable = true
            id = index
            addView(checkBox)
            addView(editText)
            setOnClickListener { checkBox.performClick() }
        }
    }

    private fun generateCheckBox(context: Context, isEnable: Boolean, isError: Boolean, index: Int): CheckBox {
        val paramsCheckBox = TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(0, 0, 12, 0) }
        return CheckBox(context).apply {
            layoutParams = paramsCheckBox
            if (isError) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    buttonTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(itemView.context, R.color.reddish)
                    )
                }
            }
            isChecked = datas.containsKey(index)
            isEnabled = isEnable
        }
    }

    private fun generateEditText(context: Context, index: Int): EditText {
        val paramsEditText = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { setMargins(4, 0, 0, 0) }
        return EditText(context).apply {
            layoutParams = paramsEditText
            isEnabled = false
            hint = context.getString(R.string.text_other)
            textSize = 12f
            setText(datas[index])
        }
    }

    private fun setError(data: DynamicView) {
        val color = if (data.isError && data.value == null) R.color.reddish else R.color.very_light_grey
        itemView.viewLine.setBackgroundColor(ContextCompat.getColor(itemView.context, color))

        if (data.isError && data.value == null)
            itemView.textError.let {
                it.text = itemView.context.getString(R.string.text_can_not_empty, data.jsonSchema.title)
                it.visible = data.isError
            }
        else if (data.isError && data.value != null)
            itemView.textError.let {
                it.text = data.errorValue
                it.visible = data.errorValue != null
            }
        else itemView.textError.visible = false
    }

    private fun setValue(value: String, isSelected: Boolean, viewIndex: Int) {
        if (isSelected) {
            datas[viewIndex] = value
        } else {
            datas.remove(viewIndex)
        }
    }

    private fun convertHashMapToList(data: HashMap<Int, String>): List<String>? {
        return if (data.isNotEmpty()) {
            val returnValue = ArrayList<String>()
            returnValue.addAll(data.values)
            returnValue
        } else {
            null
        }
    }
}
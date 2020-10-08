/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package co.sampingan.android.dynamic_ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.sampingan.android.dynamic_ui.molecule.*
import co.sampingan.android.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.molecule.*

/**
 * Created by ilgaputra15
 * on Thursday, 19/03/2020 21.48
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

class TaskSubmissionAdapter(
    private val itemClickListener: (widget: String, key: String?) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val questionIndexes: MutableList<String> = mutableListOf()
    private val questions: LinkedHashMap<String, DynamicView> = linkedMapOf()

    companion object {
        private val DESCRIPTION = FormDescriptionViewHolder.layout
        private val EDIT_TEXT = FormEditTextViewHolder.layout
        private val FILE_PHOTO = FormFilePhotoViewHolder.layout
        private val DROPDOWN = FormDropDownViewHolder.layout
        private val CHECKBOX = FormCheckBoxViewHolder.layout
        private val RADIO = FormRadioViewHolder.layout
        private val DATE_PICKER = FormDatePickerViewHolder.layout
        private val LOCATION = FormLocationViewHolder.layout
        private val RATING = FormRatingViewHolder.layout
        private val ADDRESS = FormAddressViewHolder.layout

        const val FILE_WIDGET = "file"
        const val PHOTO_WIDGET = "photo"
        const val CAMERA_WIDGET = "camera"
        const val UPDOWN_WIDGET = "updown"
        const val SELECT_WIDGET = "select"
        const val CHECKBOX_WIDGET = "checkboxes"
        const val RADIO_WIDGET = "radio"
        const val LOCATION_FIXED_WIDGET = "fixedlocation"
        const val LOCATION_DYNAMIC_WIDGET = "dynamiclocation"
        const val DATE_WIDGET = "date"
        const val DATE_TIME_WIDGET = "date-time"
        const val TIME_WIDGET = "time"
        const val TEXT_WIDGET = "text"
        const val TEXT_AREA_WIDGET = "textarea"
        const val PHONE_WIDGET = "phone"
        const val PRICE_WIDGET = "price"
        const val EMAIL_WIDGET = "email"
        const val INFORMATION_HTML_WIDGET = "information-text/html"
        const val INFORMATION_MD_WIDGET = "information-text/markdown"
        const val RATING_WIDGET = "rating"
        const val ADDRESS_WIDGET = "address"
        const val HIDDEN_WIDGET = "hidden"
    }

    fun setQuestions(list: List<DynamicView>) {
        list.forEachIndexed { index, dynamicView ->
            questions[dynamicView.componentName] = dynamicView
            questionIndexes.add(index, dynamicView.componentName)
        }
        notifyDataSetChanged()
    }

    fun updateItem(key: String) {
        notifyItemChanged(questionIndexes.indexOf(key))
    }

    /**
     * Used to notify start position of error question
     */
    fun updateItemRange(key: String) {
        notifyItemRangeChanged(questionIndexes.indexOf(key), questionIndexes.size)
    }

    fun getItemIndex(key: String) = questionIndexes.indexOf(key)

    fun refreshAdapter() {
        notifyDataSetChanged()
    }

    private fun getItem(index: Int) = questions[questionIndexes[index]]!!
    private fun getItem(key: String) = questions[key]!!

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EDIT_TEXT -> FormEditTextViewHolder(getView(parent, EDIT_TEXT))
            DESCRIPTION -> FormDescriptionViewHolder(getView(parent, DESCRIPTION))
            FILE_PHOTO -> FormFilePhotoViewHolder(getView(parent, FILE_PHOTO))
            DROPDOWN -> FormDropDownViewHolder(getView(parent, DROPDOWN))
            CHECKBOX -> FormCheckBoxViewHolder(getView(parent, CHECKBOX))
            RADIO -> FormRadioViewHolder(getView(parent, RADIO))
            DATE_PICKER -> FormDatePickerViewHolder(getView(parent, DATE_PICKER))
            LOCATION -> FormLocationViewHolder(getView(parent, LOCATION))
            RATING -> FormRatingViewHolder(getView(parent, RATING))
            ADDRESS -> FormAddressViewHolder(getView(parent, ADDRESS))
            else -> throw UnsupportedOperationException("$viewType is not registered")
        }
    }

    private fun getView(parent: ViewGroup, view: Int): View {
        return LayoutInflater.from(parent.context).inflate(view, parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        val listData = getItem(position)
        when (viewType) {
            EDIT_TEXT -> (holder as FormEditTextViewHolder).bind(listData)
            DESCRIPTION -> (holder as FormDescriptionViewHolder).bind(listData, itemClickListener)
            FILE_PHOTO -> (holder as FormFilePhotoViewHolder).bind(listData, itemClickListener)
            DROPDOWN -> (holder as FormDropDownViewHolder).bind(data = listData,
                itemClickListener = itemClickListener)
            CHECKBOX -> (holder as FormCheckBoxViewHolder).bind(listData)
            RADIO -> (holder as FormRadioViewHolder).bind(listData, itemClickListener)
            DATE_PICKER -> (holder as FormDatePickerViewHolder).bind(listData)
            LOCATION -> (holder as FormLocationViewHolder).bind(listData, itemClickListener)
            RATING -> (holder as FormRatingViewHolder).bind(listData)
            ADDRESS -> (holder as FormAddressViewHolder).bind(listData)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).uiSchemaRule.uiWidget) {
            FILE_WIDGET, PHOTO_WIDGET, CAMERA_WIDGET -> FILE_PHOTO
            UPDOWN_WIDGET -> EDIT_TEXT
            SELECT_WIDGET -> DROPDOWN
            CHECKBOX_WIDGET -> CHECKBOX
            RADIO_WIDGET -> RADIO
            LOCATION_FIXED_WIDGET, LOCATION_DYNAMIC_WIDGET -> LOCATION
            DATE_WIDGET, DATE_TIME_WIDGET, TIME_WIDGET -> DATE_PICKER
            TEXT_WIDGET, TEXT_AREA_WIDGET, PHONE_WIDGET, PRICE_WIDGET, EMAIL_WIDGET -> EDIT_TEXT
            INFORMATION_HTML_WIDGET, INFORMATION_MD_WIDGET -> DESCRIPTION
            RATING_WIDGET -> RATING
            ADDRESS_WIDGET -> ADDRESS
            else -> EDIT_TEXT
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).componentName.hashCode().toLong()
    }
}
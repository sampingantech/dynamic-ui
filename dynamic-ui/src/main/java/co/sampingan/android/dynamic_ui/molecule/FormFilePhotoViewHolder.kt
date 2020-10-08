package co.sampingan.android.dynamic_ui.molecule

//import com.google.firebase.crashlytics.FirebaseCrashlytics
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import coil.api.load
import co.sampingan.android.dynamic_ui.model.DynamicView
import co.sampingan.android.dynamic_ui.R
import co.sampingan.android.dynamic_ui.extension.visible
import co.sampingan.android.dynamic_ui.rule.toDescription
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.SLASH_TEXT
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.STAR_TEXT
import co.sampingan.android.dynamic_ui.utils.Converts.dpToPx
import co.sampingan.android.dynamic_ui.utils.FileUtils
import kotlinx.android.synthetic.main.molecule_form_file_photo.view.*


/**
 * Created by ilgaputra15
 * on Thursday, 19/03/2020 21.58
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class FormFilePhotoViewHolder(itemView: View) : BaseMoleculeViewHolder(itemView) {

    companion object {
        val layout = R.layout.molecule_form_file_photo
    }

    fun bind(data: DynamicView, itemClickListener: (widget: String, key: String) -> Unit) {
        val title =
            if (data.isRequired) "${data.jsonSchema.title} $STAR_TEXT" else data.jsonSchema.title
        itemView.textTitle.text = title
        itemView.buttonAction.setOnClickListener {
            itemClickListener.invoke(data.uiSchemaRule.uiWidget!!, data.componentName)
        }

        if (data.preview != null) {
            when (data.preview) {
                is Bitmap -> {
                    itemView.imagePreview.load(data.preview as Bitmap)
                }
                is String -> {
                    itemView.imagePreview.load(data.preview as String)
                    itemView.textFileName.let {
                        it.text =
                            if (data.fileName == null) data.preview as String else data.fileName
                        it.visible = true
                    }
                    try {
                        val dataUri = Uri.parse(data.preview as String)
                        loadUriValue(data, dataUri)
                    } catch (e: Exception) {
//                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                }
                is Uri -> {
                    loadUriValue(data, data.preview as Uri)
                }
            }
            itemView.imagePreview.visible = true

        }
        if (data.fileName != null) {
            itemView.textFileName.let {
                it.text = data.fileName
                it.visible = true
            }
        }

        itemView.buttonAction.apply {
            isEnabled = data.isEnable
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_camera_white,
                0,
                0,
                0
            )
            iconPadding = dpToPx(12f)
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

    private fun loadUriValue(data: DynamicView, uri: Uri) {
        try {
            val fileName: String?
            val selectedPath = FileUtils.getRealPath(itemView.context, uri) ?: return
            fileName = selectedPath.substring(selectedPath.lastIndexOf(SLASH_TEXT) + 1)
            data.fileName = fileName
            data.value = uri
            itemView.imagePreview.load(uri)
            itemView.textFileName.let {
                it.text = fileName
                it.visible = true
            }
        } catch (e: Exception) {
//            FirebaseCrashlytics.getInstance().recordException(e)
            e.printStackTrace()
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
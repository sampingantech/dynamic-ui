package com.sampingan.agentapp.dynamic_ui.molecule

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sampingan.agentapp.dynamic_ui.model.DynamicView
import com.sampingan.agentapp.dynamic_ui.R
import com.sampingan.agentapp.dynamic_ui.extension.fullExpanded
import com.sampingan.agentapp.dynamic_ui.extension.visible
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.PERCENT_TEXT
import io.noties.markwon.*
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.ImageProps
import io.noties.markwon.image.ImageSize
import io.noties.markwon.image.coil.CoilImagesPlugin
import kotlinx.android.synthetic.main.dialog_criteria_submission.view.*
import kotlinx.android.synthetic.main.dialog_criteria_submission.view.textTitle
import org.commonmark.node.Image

/**
 * Created by ilgaputra15
 * on Tuesday, 14/04/2020 10.56
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

abstract class BaseMoleculeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    BaseMolecule {

    private lateinit var markwon: Markwon


    @SuppressLint("InflateParams")
    override fun criteriaDialog(data: DynamicView) {
        val bottomDialog = BottomSheetDialog(itemView.context)
        val layout =
            LayoutInflater.from(itemView.context).inflate(R.layout.dialog_criteria_submission, null)
        bottomDialog.setContentView(layout)
        bottomDialog.show()
        bottomDialog.fullExpanded(layout)

        val title = data.jsonSchema.title
        val helpImage = data.uiSchemaRule.uiHelpImage
        val help = data.uiSchemaRule.uiHelp

        layout.textTitle.text = title
        layout.cardImageCriteria.visible = helpImage != null
        if (helpImage != null) {
            layout.imageCriteria.load(helpImage) {
                error(R.mipmap.ic_launcher)
            }
        }

        layout.textCriteria.let {
            markwon = buildMarkwon(
                context = itemView.context,
                itemClickListener = { widget, key -> },
                uiWidget = data.uiSchemaRule.uiWidget!!
            )
            markwon.setMarkdown(it, help.orEmpty())
            it.visible = help.isNullOrEmpty().not()
        }
    }

    protected fun buildMarkwon(
        context: Context,
        itemClickListener: (widget: String, key: String) -> Unit,
        uiWidget: String
    ): Markwon {
        return Markwon.builder(context)
            .usePlugin(CoilImagesPlugin.create(context))
            .usePlugin(createAbstractMarkwonPlugin(itemClickListener, uiWidget))
            .usePlugin(HtmlPlugin.create())
            .build()
    }

    private fun createAbstractMarkwonPlugin(
        itemClickListener: (widget: String, key: String) -> Unit,
        uiWidget: String
    ): AbstractMarkwonPlugin {
        return object : AbstractMarkwonPlugin() {
            override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                builder.linkResolver { view, link ->
                    itemClickListener(uiWidget, link)
                }
            }

            override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
                builder.setFactory(Image::class.java) { configuration, props ->
                    AsyncDrawableSpan(
                        configuration.theme(),
                        AsyncDrawable(
                            ImageProps.DESTINATION.require(props),
                            configuration.asyncDrawableLoader(),
                            configuration.imageSizeResolver(),
                            imageSize(props)
                        ),
                        AsyncDrawableSpan.ALIGN_BOTTOM,
                        ImageProps.REPLACEMENT_TEXT_IS_LINK[props, false]
                    )
                }
            }
        }
    }

    private fun imageSize(props: RenderProps): ImageSize? {
        val imageSize = ImageProps.IMAGE_SIZE[props]
        return imageSize ?: ImageSize(ImageSize.Dimension(100f, PERCENT_TEXT), null)
    }

}

interface BaseMolecule {
    fun criteriaDialog(data: DynamicView)
}
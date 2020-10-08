/*
 * Copyright (c) 2020. 
 * PT. Sampingan Mitra Indonesia
 */

package co.sampingan.android.dynamic_ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import co.sampingan.android.dynamic_ui.R
import co.sampingan.android.dynamic_ui.extension.visible
import kotlinx.android.synthetic.main.dialog_full_screen.*

/**
 * Created by ilgaputra15
 * on Saturday, 28/03/2020 20.37
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

class FullScreenDialog : DialogFragment() {

    private var callback: DialogListener? = null
    private var imageResource: Int? = null
    private var title: String? = null
    private var desc: String? = null
    private var buttonText: String? = null

    fun setDialogListener(callback: DialogListener) {
        this.callback = callback
    }

    companion object {
        private const val IMAGE_RESOURCE = "ImageResource"
        private const val TITLE = "Title"
        private const val DESC = "Desc"
        private const val BUTTON_TEXT = "ButtonText"

        fun newInstance(
            imageResource: Int = 0,
            title: String? = null,
            desc: String? = null,
            buttonText: String? = null
        ): FullScreenDialog {
            val fullScreenDialog = FullScreenDialog()
            val args = Bundle()
            args.putInt(IMAGE_RESOURCE, imageResource)
            args.putString(TITLE, title)
            args.putString(DESC, desc)
            args.putString(BUTTON_TEXT, buttonText)
            fullScreenDialog.arguments = args
            return fullScreenDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val image = arguments?.getInt(IMAGE_RESOURCE) ?: 0
        imageResource = if (image != 0) image else null
        title = arguments?.getString(TITLE)
        desc = arguments?.getString(DESC)
        buttonText = arguments?.getString(BUTTON_TEXT)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.dialog_full_screen, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogFullScreenTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageResource?.let { image ->
            activity?.let { activity ->
                imageDialog.setImageDrawable(ContextCompat.getDrawable(activity, image))
            }
        }

        imageDialog.visible = imageResource != null

        textTitle.let {
            it.visible = title != null
            it.text = title
        }

        textDesc.let {
            it.visible = desc != null
            it.text = desc
        }

        buttonAction.text = buttonText ?: activity?.getString(R.string.text_oke)
        buttonAction.setOnClickListener {
            callback?.onDialogFinish()
            dismiss()
        }
    }

    interface DialogListener {
        fun onDialogFinish()
    }
}
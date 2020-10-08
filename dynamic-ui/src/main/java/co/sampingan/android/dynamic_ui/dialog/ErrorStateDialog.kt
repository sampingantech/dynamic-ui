/*
 * Copyright (c) 2020. 
 * PT. Sampingan Mitra Indonesia
 */

package co.sampingan.android.dynamic_ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import co.sampingan.android.dynamic_ui.R
import co.sampingan.android.dynamic_ui.extension.fullExpanded
import co.sampingan.android.dynamic_ui.extension.visible
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.PERMISSION_CAMERA_TEXT
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.PERMISSION_LOCATION_TEXT
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.PERMISSION_STORAGE_TEXT
import kotlinx.android.synthetic.main.dialog_connection_error.view.*
import kotlinx.android.synthetic.main.dialog_permission.view.*

/**
 * Created by ilgaputra15
 * on Sunday, 29/03/2020 14.19
 * Mobile Engineer - https://github.com/ilgaputra15
 **/
class ErrorStateDialog(
    val activity: Activity,
    private val itemCallback: (request: Int) -> Unit
) {

    companion object {
        private const val SESSION_TIME_OUT_TITLE = "Ups, sesi kamu telah berakhir"
        private const val SESSION_TIME_OUT_DESC =
            "Harap masuk kembali menggunakan nomor telepon kamu yang telah terdaftar dalam akun Sampingan."
        private const val DATA_ERROR_TITLE = "Gagal menampilkan data"
        private const val DATA_ERROR_DESC =
            "Data yang kamu inginkan saat ini belum dapat diakses, coba lagi lain kali."
        private const val CONNECTION_ERROR_TITLE = "Terjadi kesalahan jaringan"
        private const val CONNECTION_ERROR_DESC = "Silahkan periksa koneksi internet kamu."
        private const val OFFLINE_TITLE = "Kamu sedang offline"
        private const val OFFLINE_DESC =
            "Harap periksa kembali jaringan kamu. Jangan khawatir! Perubahan yang kamu lakukan akan dikirim saat kamu kembali online."
    }

    private lateinit var bottomDialog: BottomSheetDialog

    fun showSessionTimeout(requestCode: Int) {
        showDialog(
            requestCode,
            image = ContextCompat.getDrawable(activity, R.drawable.ic_session_timeout),
            title = SESSION_TIME_OUT_TITLE,
            desc = SESSION_TIME_OUT_DESC,
            buttonText = activity.getString(R.string.text_im_understand),
            isCallbackOnLostFocus = true
        )
    }

    fun showGetDataError(requestCode: Int) {
        showDialog(
            requestCode,
            image = ContextCompat.getDrawable(activity, R.drawable.ic_lost_connection),
            title = DATA_ERROR_TITLE,
            desc = DATA_ERROR_DESC,
            buttonText = activity.getString(R.string.text_im_understand),
            isCallbackOnLostFocus = false
        )
    }

    fun showConnectionError(requestCode: Int) {
        showDialog(
            requestCode,
            image = ContextCompat.getDrawable(activity, R.drawable.ic_lost_connection),
            title = CONNECTION_ERROR_TITLE,
            desc = CONNECTION_ERROR_DESC,
            buttonText = activity.getString(R.string.text_im_understand),
            isCallbackOnLostFocus = false
        )
    }

    fun showOffline(requestCode: Int) {
        showDialog(
            requestCode,
            image = ContextCompat.getDrawable(activity, R.drawable.ic_offline),
            title = OFFLINE_TITLE,
            desc = OFFLINE_DESC,
            buttonText = activity.getString(R.string.text_im_understand),
            isCallbackOnLostFocus = false
        )
    }

    fun permissionDeniedDialog(
        permission: String,
        imageDrawable: Int? = null,
        callback: () -> Unit
    ) {
        val value = when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION -> PERMISSION_LOCATION_TEXT
            Manifest.permission.CAMERA -> PERMISSION_CAMERA_TEXT
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE -> PERMISSION_STORAGE_TEXT
            else -> permission
        }
        if (::bottomDialog.isInitialized && bottomDialog.isShowing) bottomDialog.dismiss()
        bottomDialog = BottomSheetDialog(activity)
        val layoutTutorial = View.inflate(activity, R.layout.dialog_permission, null)
        bottomDialog.setContentView(layoutTutorial)
        bottomDialog.show()
        bottomDialog.fullExpanded(layoutTutorial)
        with(layoutTutorial) {
            textTitle.text = activity.getString(R.string.dialog_current_location_title, value)
            textDesc.text = activity.getString(R.string.dialog_current_location_desc, value)
            imageView.visible = imageDrawable != null
            if (imageDrawable != null)
                imageView.setImageDrawable(ContextCompat.getDrawable(activity, imageDrawable))
            buttonCancel.setOnClickListener { bottomDialog.dismiss() }
            buttonToSetting.setOnClickListener {
                callback.invoke()
                bottomDialog.dismiss()
            }
        }
    }

    @SuppressLint("InflateParams")
    fun showDialog(
        requestCode: Int = 0,
        image: Drawable? = null,
        title: String? = null,
        desc: String? = null,
        buttonText: String? = null,
        isCallbackOnLostFocus: Boolean = true
    ) {
        bottomDialog = BottomSheetDialog(activity)
        val layout = LayoutInflater.from(activity).inflate(R.layout.dialog_connection_error, null)
        bottomDialog.setContentView(layout)
        layout.imageDialog.let {
            it.setImageDrawable(image)
            it.visible = image != null
        }
        layout.textDialogTitle.let {
            it.text = title
            it.visible = title != null
        }
        layout.textDialogDesc.let {
            it.visible = desc != null
            it.text = desc
        }
        if (buttonText != null) layout.buttonAction.text = buttonText
        bottomDialog.show()
        bottomDialog.fullExpanded(layout)
        layout.buttonAction.setOnClickListener {
            if (!isCallbackOnLostFocus) itemCallback.invoke(requestCode)
            bottomDialog.dismiss()
        }
        bottomDialog.setOnDismissListener {
            if (isCallbackOnLostFocus) itemCallback.invoke(requestCode)
        }
    }
}
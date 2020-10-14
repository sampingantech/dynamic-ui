package co.sampingan.android.dynamic_ui

import android.content.Intent

/**
 * Created by ilgaputra15
 * on Tuesday, 24/03/2020 11.50
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

interface FormInterface {
    fun setupForm()
    fun showFileDialog(widget: String, key: String)
    fun showLocation(key: String, isFixLocation: Boolean = false)
    fun locationData(intent: Intent?)
}
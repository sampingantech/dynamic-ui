package com.sampingan.agentapp.dynamic_ui.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * Created by ilgaputra15
 * on Thursday, 12/03/2020 17.58
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

fun EditText.toNextEditText(editText: EditText) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {}

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (s.isNotEmpty()) {
                editText.requestFocus()
                editText.selectAll()
            }
        }
    })
}

fun EditText.afterTextChanged(textAfterChange: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            textAfterChange.invoke(editable)
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
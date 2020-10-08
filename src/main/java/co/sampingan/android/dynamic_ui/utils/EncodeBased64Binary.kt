package co.sampingan.android.dynamic_ui.utils

import android.graphics.Bitmap
import android.util.Base64
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.BASE64_PATTERN
import java.io.*

/**
 * Created by ilgaputra15
 * on Friday, 27/03/2020 14.13
 * Mobile Engineer - https://github.com/ilgaputra15
 **/

object EncodeBased64Binary {
    fun bitmapToBase64(bitmap: Bitmap?): String {
        if (bitmap == null) return ""
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        return if (base64.isNotEmpty()) "$BASE64_PATTERN:$base64" else ""
    }

    fun getBase64FromPath(path: String): String {
        var base64 = ""
        if (path.isEmpty()) return ""
        try {
            val file = File(path)
            val buffer = ByteArray(file.length().toInt() + 100)
            val length = FileInputStream(file).read(buffer)
            base64 = Base64.encodeToString(
                buffer, 0, length,
                Base64.NO_WRAP
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return if (base64.isNotEmpty()) "$BASE64_PATTERN:$base64" else ""
    }

}
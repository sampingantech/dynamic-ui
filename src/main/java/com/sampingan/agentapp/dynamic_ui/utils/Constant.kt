/*
 * Copyright (c) 2020.
 * PT. Sampingan Mitra Indonesia
 */

package com.sampingan.agentapp.dynamic_ui.utils

class Constant {
    companion object {
        const val IN_LANGUANGE = "in"
        const val RCF3666_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
        const val DATE_PATTERN = "dd MMM yyyy"
        const val TIME_PATTERN = "HH:mm:ss"
        const val DATE_TIME_PATTERN = "dd MMM yyyy HH:mm:ss"
        const val FULL_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        const val BASE64_PATTERN = "Base64"
        const val ID_SELECTION = "_id=?"
        const val DATA_SELECTION = "_data"
        const val EMAIL_REGEX_VALIDATION =
            "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$"

        const val PHONE_PREFIX = "+62"
        const val PRICE_PREFIX = "Rp"

        const val URI_PUBLIC_DOWNLOAD = "content://downloads/public_downloads"
        const val URI_AUTHORITY_EXTERNAL = "com.android.externalstorage.documents"
        const val URI_AUTHORITY_DOWNLOAD = "com.android.providers.downloads.documents"
        const val URI_AUTHORITY_MEDIA = "com.android.providers.media.documents"
        const val URI_AUTHORITY_GOOGLE_PHOTO = "com.google.android.apps.photos.content"


        const val INTEGER_TEXT = "integer"
        const val BOOLEAN_TEXT = "boolean"
        const val PRIMARY_TEXT = "primary"
        const val STORAGE_TEXT = "storage"
        const val DOWNLOAD_TEXT = "Download"
        const val DEFAULT_OTHER_VALUE = "Lainnya"
        const val DEFAULT_YES_VALUE = "Ya"
        const val DEFAULT_NO_VALUE = "Tidak"
        const val PERMISSION_LOCATION_TEXT = "Lokasi"
        const val PERMISSION_CAMERA_TEXT = "Kamera"
        const val PERMISSION_STORAGE_TEXT = "Penyimpanan"

        const val IMAGE_TEXT = "image"
        const val CAMERA_TEXT = "camera"
        const val PHOTO_TEXT = "photo"
        const val VIDEO_TEXT = "video"
        const val AUDIO_TEXT = "audio"
        const val CONTENT_TEXT = "content"
        const val FILE_TEXT = "file"

        const val COLON_TEXT = ":"
        const val SEMICOLON_TEXT = ";"
        const val STAR_TEXT = "*"
        const val SLASH_TEXT = "/"
        const val PERCENT_TEXT = "%"
        const val DASH_TEXT = "-"

        //JSON SCHEMA KEY UTILS
        const val ENUM_KEY = "enum"
    }
}
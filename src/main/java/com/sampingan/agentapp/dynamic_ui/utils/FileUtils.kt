package com.sampingan.agentapp.dynamic_ui.utils

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.AUDIO_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.COLON_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.CONTENT_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DATA_SELECTION
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.DOWNLOAD_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.FILE_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.ID_SELECTION
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.IMAGE_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.PRIMARY_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.SLASH_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.STORAGE_TEXT
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.URI_AUTHORITY_DOWNLOAD
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.URI_AUTHORITY_EXTERNAL
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.URI_AUTHORITY_GOOGLE_PHOTO
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.URI_AUTHORITY_MEDIA
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.URI_PUBLIC_DOWNLOAD
import com.sampingan.agentapp.dynamic_ui.utils.Constant.Companion.VIDEO_TEXT

object FileUtils {
    fun getRealPath(context: Context, fileUri: Uri): String? {
        // SDK < API11
        return if (Build.VERSION.SDK_INT < 19) {
            getRealPathFromURIAPI11to18(context, fileUri)
        } else {
            getRealPathFromURIAPI19(context, fileUri)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun getRealPathFromURIAPI11to18(
        context: Context?,
        contentUri: Uri?
    ): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null
        val cursorLoader =
            CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun getRealPathFromURIAPI19(context: Context, uri: Uri): String? {

        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            when {
                isExternalStorageDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(COLON_TEXT.toRegex()).toTypedArray()
                    val type = split[0]

                    // This is for checking Main Memory
                    return if (PRIMARY_TEXT.equals(type, ignoreCase = true)) {
                        if (split.size > 1) {
                            Environment.getExternalStorageDirectory()
                                .toString() + SLASH_TEXT + split[1]
                        } else {
                            Environment.getExternalStorageDirectory().toString() + SLASH_TEXT
                        }
                        // This is for checking SD Card
                    } else {
                        STORAGE_TEXT + SLASH_TEXT + docId.replace(COLON_TEXT, SLASH_TEXT)
                    }
                }
                isDownloadsDocument(uri) -> {
                    val fileName =
                        getFilePath(context, uri)
                    if (fileName != null) {
                        return Environment.getExternalStorageDirectory()
                            .toString() + "$SLASH_TEXT$DOWNLOAD_TEXT$SLASH_TEXT" + fileName
                    }
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse(URI_PUBLIC_DOWNLOAD), id.toLong()
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        null,
                        null
                    )
                }
                isMediaDocument(uri) -> {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(COLON_TEXT.toRegex()).toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    when (type) {
                        IMAGE_TEXT -> {
                            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        }
                        VIDEO_TEXT -> {
                            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        }
                        AUDIO_TEXT -> {
                            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                        }
                    }
                    val selection = ID_SELECTION
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(
                        context,
                        contentUri,
                        selection,
                        selectionArgs
                    )
                }
            }
        } else if (CONTENT_TEXT.equals(uri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if (FILE_TEXT.equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = DATA_SELECTION
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun getFilePath(
        context: Context,
        uri: Uri
    ): String? {
        val projection = arrayOf(
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        context.contentResolver.query(
            uri, projection, null, null,
            null
        ).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
                return cursor.getString(index)
            }
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return URI_AUTHORITY_EXTERNAL == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return URI_AUTHORITY_DOWNLOAD == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return URI_AUTHORITY_MEDIA == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return URI_AUTHORITY_GOOGLE_PHOTO == uri.authority
    }
}
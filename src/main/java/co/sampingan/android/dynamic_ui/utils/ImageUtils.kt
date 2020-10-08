/*
 * Copyright (c) 2020. 
 * PT. Sampingan Mitra Indonesia
 */

package co.sampingan.android.dynamic_ui.utils

/**
 * Created by ilgaputra15
 * on Tuesday, 16/07/2019 12:30
 * Division Mobile - PT.Homecareindo Global Medika
 **/

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import co.sampingan.android.dynamic_ui.utils.Constant.Companion.SLASH_TEXT
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageUtils(activity: Context) {
    private var context: Context = activity

    /**
     * Check Camera Availability
     *
     * @return
     */
    val isDeviceSupportCamera: Boolean
        get() = this.context.packageManager.hasSystemFeature(
            PackageManager.FEATURE_CAMERA_ANY)

    /**
     * Compress Imgae
     *
     * @param imageUri
     * @param height
     * @param width
     * @return
     */
    fun compressImage(imageUri: String, height: Float, width: Float): Bitmap? {

        val filePath = getRealPathFromURI(imageUri)
        var scaledBitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        // by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
        // you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true
        var bmp = BitmapFactory.decodeFile(filePath, options)

        var actualHeight = options.outHeight
        var actualWidth = options.outWidth
        // max Height and width values of the compressed image is taken as 816x612
        var imgRatio = (actualWidth / actualHeight).toFloat()
        val maxRatio = width / height
        // width and height values are set maintaining the aspect ratio of the image
        if (actualHeight > height || actualWidth > width) {
            when {
                imgRatio < maxRatio -> {
                    imgRatio = height / actualHeight
                    actualWidth = (imgRatio * actualWidth).toInt()
                    actualHeight = height.toInt()
                }
                imgRatio > maxRatio -> {
                    imgRatio = width / actualWidth
                    actualHeight = (imgRatio * actualHeight).toInt()
                    actualWidth = width.toInt()
                }
                else -> {
                    actualHeight = height.toInt()
                    actualWidth = width.toInt()
                }
            }
        }
        //  setting inSampleSize value allows to load a scaled down version of the original image
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        //  inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false
        // this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true
        options.inInputShareable = true
        options.inTempStorage = ByteArray(16 * 1024)

        try {
            bmp = BitmapFactory.decodeFile(filePath, options)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        try {
            scaledBitmap = Bitmap.createScaledBitmap(bmp, actualWidth, actualHeight, false)
        } catch (exception: OutOfMemoryError) {
            exception.printStackTrace()
        }

        // check the rotation of the image and display it properly
        val exif: ExifInterface
        try {
            exif = ExifInterface(filePath!!)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
            val matrix = Matrix()
            when (orientation) {
                6 -> matrix.postRotate(90f)
                3 -> matrix.postRotate(180f)
                8 -> matrix.postRotate(270f)
            }
            scaledBitmap = scaledBitmap?.let {
                Bitmap.createBitmap(
                    it,
                    0,
                    0,
                    scaledBitmap!!.width,
                    scaledBitmap!!.height,
                    matrix,
                    true
                )
            }
            return scaledBitmap
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * ImageSize Calculation
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    /**
     * Get Path from Image URI
     *
     * @param uri
     * @return
     */
    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.context.contentResolver.query(uri!!, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val path = cursor.getString(columnIndex)
            cursor.close()
            path
        } else
            uri.path
    }

    /**
     * Get RealPath from Content URI
     *
     * @param contentURI
     * @return
     */
    @SuppressLint("Recycle")
    private fun getRealPathFromURI(contentURI: String?): String? {
        val contentUri = Uri.parse(contentURI)
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        return if (cursor == null) {
            contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(index)
        }
    }


    /**
     * Get image from Uri
     *
     * @param uri
     * @param height
     * @param width
     * @return
     */
    fun getImageFromUri(uri: Uri, height: Float, width: Float): Bitmap? {
        var bitmap: Bitmap? = null

        try {
            bitmap = compressImage(uri.toString(), height, width)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * Get filename from URI
     *
     * @param uri
     * @return
     */
    fun getFileNameFromUri(uri: Uri): String? {
        val path: String?
        val fileName: String?
        try {
            path = getRealPathFromURI(uri.path)
            fileName = path!!.substring(path.lastIndexOf(SLASH_TEXT) + 1)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return fileName
    }


    /**
     * Check Image Exist (or) Not
     *
     * @param file_name
     * @param file_path
     * @return
     */
    fun checkImage(file_name: String, file_path: String): Boolean {
        val path = File(file_path)
        val file = File(path, file_name)
        return file.exists()
    }


    /**
     * Get Image from the given path
     *
     * @param file_name
     * @param file_path
     * @return
     */
    fun getImage(file_name: String, file_path: String): Bitmap? {
        val path = File(file_path)
        val file = File(path, file_name)
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.ARGB_8888
        options.inSampleSize = 2
        options.inTempStorage = ByteArray(16 * 1024)
        return BitmapFactory.decodeFile(file.absolutePath, options)
    }

    /**
     * Create an image
     *
     * @param bitmap
     * @param file_name
     * @param filepath
     * @param file_replace
     */
    fun createImage(
        bitmap: Bitmap,
        file_name: String,
        filepath: String,
        file_replace: Boolean
    ): File {

        val path = File(filepath)

        if (!path.exists()) {
            path.mkdirs()
        }

        var file = File(path, file_name)

        if (file.exists()) {
            if (file_replace) {
                file.delete()
                file = File(path, file_name)
                storeImage(file, bitmap)
            }
        } else {
            storeImage(file, bitmap)
        }
        return file
    }

    /**
     * @param file
     * @param bmp
     */
    private fun storeImage(file: File, bmp: Bitmap) {
        try {
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

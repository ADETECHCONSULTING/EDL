package fr.atraore.edl.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import fr.atraore.edl.ui.edl.constat.signature.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream

class InsertMedia {

    companion object {
        fun insertImage(
            cr: ContentResolver,
            source: Bitmap?,
            title: String?,
            description: String?
        ): String? {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, title)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, title)
            values.put(MediaStore.Images.Media.DESCRIPTION, description)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            // Add the date meta data to ensure the image is added at the front of the gallery
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            var url: Uri? = null
            var stringUrl: String? = null /* value to be returned */
            try {
                url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                if (source != null) {
                    val imageOut: OutputStream? = cr.openOutputStream(url!!)
                    try {
                        source.compress(Bitmap.CompressFormat.JPEG, 50, imageOut)
                    } finally {
                        imageOut?.close()
                    }
                    val id = ContentUris.parseId(url)
                    // Wait until MINI_KIND thumbnail is generated.
                    val miniThumb: Bitmap = MediaStore.Images.Thumbnails.getThumbnail(cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null)
                    // This is for backward compatibility.
                    storeThumbnail(cr, miniThumb, id, 50f, 50f, MediaStore.Images.Thumbnails.MICRO_KIND)
                } else {
                    cr.delete(url!!, null, null)
                    url = null
                }
            } catch (e: Exception) {
                if (url != null) {
                    cr.delete(url, null, null)
                    url = null
                }
            }
            if (url != null) {
                stringUrl = url.toString()
            }
            return stringUrl
        }


        fun loadPhotoFromInternalStorage(activity: Activity, filename: String): Bitmap {
            val files = activity.filesDir.listFiles()
            val file = files.find { file -> file.name.contains(filename) }.let {
                val bytes = it!!.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            }
            return file.bmp
        }

        /**
         * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
         * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
         * meta data. The StoreThumbnail method is private so it must be duplicated here.
         * @see android.provider.MediaStore.Images.Media
         */
        private fun storeThumbnail(
            cr: ContentResolver,
            source: Bitmap,
            id: Long,
            width: Float,
            height: Float,
            kind: Int
        ): Bitmap? {

            // create the matrix to scale it
            val matrix = Matrix()
            val scaleX = width / source.width
            val scaleY = height / source.height
            matrix.setScale(scaleX, scaleY)
            val thumb = Bitmap.createBitmap(
                source, 0, 0,
                source.width,
                source.height, matrix,
                true
            )
            val values = ContentValues(4)
            values.put(MediaStore.Images.Thumbnails.KIND, kind)
            values.put(MediaStore.Images.Thumbnails.IMAGE_ID, id.toInt())
            values.put(MediaStore.Images.Thumbnails.HEIGHT, thumb.height)
            values.put(MediaStore.Images.Thumbnails.WIDTH, thumb.width)
            val url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values)
            return try {
                val thumbOut: OutputStream? = cr.openOutputStream(url!!)
                thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut)
                thumbOut?.close()
                thumb
            } catch (ex: FileNotFoundException) {
                null
            } catch (ex: IOException) {
                null
            }
        }
    }
}
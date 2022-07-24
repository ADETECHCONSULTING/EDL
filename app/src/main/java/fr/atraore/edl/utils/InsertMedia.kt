package fr.atraore.edl.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import fr.atraore.edl.ui.edl.constat.signature.InternalStoragePhoto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream

class InsertMedia {

    companion object {

        fun savePhotoToInternalStorage(activity: Activity, filename: String, bmp: Bitmap): Boolean {
            return try {
                activity.openFileOutput("$filename.png", Context.MODE_PRIVATE).use { stream ->
                    if(!bmp.compress(Bitmap.CompressFormat.PNG, 95, stream)) {
                        throw IOException("Couldn't save bitmap.")
                    }
                }
                true
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }

        fun saveFileToInternalStorage(activity: Activity, filename: String, file: File, extension: String): Boolean {
            return try {
                activity.openFileOutput("$filename.$extension", Context.MODE_PRIVATE).use { stream ->
                    stream.write(file.readBytes())
                }
                true
            } catch(e: IOException) {
                e.printStackTrace()
                false
            }
        }

        fun loadPhotoFromInternalStorage(activity: Activity, filename: String): Bitmap? {
            val files = activity.filesDir.listFiles()
            val file = files.find { file -> file.name.contains(filename) }?.let {
                val bytes = it.readBytes()
                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                InternalStoragePhoto(it.name, bmp)
            }
            return file?.bmp
        }

        fun deletePhotoFromInternalStorage(activity: Activity, filename: String) {
            activity.deleteFile(filename)
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
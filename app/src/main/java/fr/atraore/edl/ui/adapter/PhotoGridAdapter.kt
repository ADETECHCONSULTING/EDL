package fr.atraore.edl.ui.adapter

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import fr.atraore.edl.R
import fr.atraore.edl.ui.edl.constat.second_page.detail.DetailEndConstatViewModel
import fr.atraore.edl.utils.InsertMedia.Companion.deletePhotoFromInternalStorage
import fr.atraore.edl.utils.InsertMedia.Companion.loadPhotoFromInternalStorage
import kotlinx.android.synthetic.main.photo_grid_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class PhotoGridAdapter(private val activity: Activity, private val viewModel: DetailEndConstatViewModel) : RecyclerView.Adapter<PhotoGridAdapter.PhotoGridViewHolder>(), CoroutineScope {

    private var data: List<String> = ArrayList()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoGridViewHolder {
        return PhotoGridViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_grid_item, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PhotoGridViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<String>) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class PhotoGridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) = with(itemView) {

            val bitmap = getBitmapFromUri(Uri.parse(item))
            addImageInView(bitmap, itemView.imv_photo)

            itemView.btn_delete.setOnClickListener { view ->
                deletePhotoFromAdapterAndLocal(item)
            }
        }

        private fun addImageInView(bitmap: Bitmap?, imageView: ImageView) {
            if (bitmap !== null) {
                val targetBmp = Bitmap.createScaledBitmap(bitmap, 400, 400, false)

                if (targetBmp !== null) {
                    imageView.setImageBitmap(targetBmp)
                }
            }
        }

        private fun getBitmapFromUri(imageUri: Uri): Bitmap? {
            return loadPhotoFromInternalStorage(activity, imageUri.toString())
        }

        private fun deletePhotoFromAdapterAndLocal(filename: String) {
            data.filter { fileN -> fileN != filename }.let {
                launch {
                    viewModel.updateImagesPaths(it.joinToString(","), viewModel.currentDetail.idDetail)
                }
                deletePhotoFromInternalStorage(activity, filename)
            }
        }
    }
}
package fr.atraore.edl.photo.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import fr.atraore.edl.R
import fr.atraore.edl.photo.loader.ImageLoader


class GlideImageLoader: ImageLoader {

    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        Glide.with(context)
            .load(uri)
            .asBitmap()
            .placeholder(R.drawable.bg_placeholder)
            .centerCrop()
            .into(view)
    }
}
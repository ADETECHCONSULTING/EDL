package fr.atraore.edl.utils

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.listeners.EventHook

//assited viewmodels
inline fun <reified T : ViewModel> Fragment.assistedViewModel(
    crossinline viewModelProducer: (SavedStateHandle) -> T
) = viewModels<T> {
    object : AbstractSavedStateViewModelFactory(this, arguments) {
        override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) =
            viewModelProducer(handle) as T
    }
}

//Observable once
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

//check not null
fun <T: Any, R: Any> whenAllNotNull(vararg options: T?, block: (List<T>)->R) {
    if (options.all { it != null }) {
        block(options.filterNotNull()) // or do unsafe cast to non null collection
    }
}

fun <T: Any, R: Any> whenAnyNotNull(vararg options: T?, block: (List<T>)->R) {
    if (options.any { it != null }) {
        block(options.filterNotNull())
    }
}

abstract class ClickEventHook<Item : GenericItem> : EventHook<Item> {
    abstract fun onClick(v: View, position: Int, fastAdapter: FastAdapter<Item>, item: Item)
}

/**
 * Convenient extension function to simplify adding a [ClickEventHook] to the [FastAdapter]
 */
inline fun <reified VH : RecyclerView.ViewHolder, reified Item : GenericItem> FastAdapter<GenericItem>.addClickListener(crossinline resolveView: (VH) -> View?, crossinline resolveViews: ((VH) -> List<View>?) = { null }, crossinline onClick: (v: View, position: Int, fastAdapter: FastAdapter<Item>, item: Item) -> Unit) {
    addEventHook(object : ClickEventHook<Item>() {
        override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
            return if (viewHolder is VH) resolveView.invoke(viewHolder) else super.onBind(viewHolder)
        }

        override fun onBindMany(viewHolder: RecyclerView.ViewHolder): List<View>? {
            return if (viewHolder is VH) resolveViews.invoke(viewHolder) else super.onBindMany(viewHolder)
        }

        override fun onClick(v: View, position: Int, fastAdapter: FastAdapter<Item>, item: Item) {
            onClick.invoke(v, position, fastAdapter, item)
        }
    })
}

// start of extension.
fun View.toBitmap(onBitmapReady: (Bitmap) -> Unit, onBitmapError: (Exception) -> Unit) {

    try {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val temporalBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

            // Above Android O, use PixelCopy due
            // https://stackoverflow.com/questions/58314397/
            val window: Window = (this.context as Activity).window

            val location = IntArray(2)

            this.getLocationInWindow(location)

            val viewRectangle = Rect(location[0], location[1], location[0] + this.width, location[1] + this.height)

            val onPixelCopyListener: PixelCopy.OnPixelCopyFinishedListener = PixelCopy.OnPixelCopyFinishedListener { copyResult ->

                if (copyResult == PixelCopy.SUCCESS) {

                    onBitmapReady(temporalBitmap)
                } else {

                    error("Error while copying pixels, copy result: $copyResult")
                }
            }

            PixelCopy.request(window, viewRectangle, temporalBitmap, onPixelCopyListener, Handler(Looper.getMainLooper()))
        } else {

            val temporalBitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)

            val canvas = android.graphics.Canvas(temporalBitmap)

            this.draw(canvas)

            canvas.setBitmap(null)

            onBitmapReady(temporalBitmap)
        }

    } catch (exception: Exception) {

        onBitmapError(exception)
    }
}
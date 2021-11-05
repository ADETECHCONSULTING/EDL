package fr.atraore.edl

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.atraore.edl.data.AppDatabase
import fr.atraore.edl.photo.PhotoPicker
import fr.atraore.edl.photo.utils.GlideImageLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class EdlApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        PhotoPicker.init(
            loader = GlideImageLoader(),
            authority = "fr.atraore.edl.fileprovider"
        )
    }
}
package fr.atraore.edl.photo.utils

import android.content.Context
import androidx.core.content.FileProvider
import fr.atraore.edl.photo.PickerConfiguration
import java.io.File

internal fun File.providerUri(context: Context) =
    FileProvider.getUriForFile(context, PickerConfiguration.getAuthority(), this)
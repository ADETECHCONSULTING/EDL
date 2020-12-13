package fr.atraore.edl.data.models

import androidx.room.Embedded

data class ConstatWithDetails(
    @Embedded val constat: Constat,

)
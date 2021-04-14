package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "agencyId"])
data class ConstatAgencyCrossRef(
    val constatId: String,
    val agencyId: String
)
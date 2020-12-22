package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "contractorId"])
data class ConstatContractorCrossRef(
    val constatId: String,
    val contractorId: String
)
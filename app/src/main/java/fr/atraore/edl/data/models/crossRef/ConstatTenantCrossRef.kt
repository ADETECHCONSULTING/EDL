package fr.atraore.edl.data.models.crossRef

import androidx.room.Entity

@Entity(primaryKeys = ["constatId", "tenantId"])
data class ConstatTenantCrossRef(
    val constatId: Int,
    val tenantId: Int
)
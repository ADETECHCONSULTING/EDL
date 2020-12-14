package fr.atraore.edl.data.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import fr.atraore.edl.data.models.crossRef.ConstatContractorCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatOwnerCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatPropertyCrossRef
import fr.atraore.edl.data.models.crossRef.ConstatTenantCrossRef

data class ConstatWithDetails(
    @Embedded val constat: Constat,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "ownerId",
        associateBy = Junction(ConstatOwnerCrossRef::class)
    )
    val owners: List<Owner>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "tenantId",
        associateBy = Junction(ConstatTenantCrossRef::class)
    )
    val tenants: List<Tenant>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "contractorId",
        associateBy = Junction(ConstatContractorCrossRef::class)
    )
    val contractors: List<Contractor>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "propertyId",
        associateBy = Junction(ConstatPropertyCrossRef::class)
    )
    val properties: List<Property>
)
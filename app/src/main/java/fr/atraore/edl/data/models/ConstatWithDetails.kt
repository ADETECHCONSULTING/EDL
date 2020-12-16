package fr.atraore.edl.data.models

import androidx.room.Embedded
import androidx.room.Ignore
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
) {
    @Ignore
    fun getOwnersConcatenate(): String {
        var res = "";

        if (owners.isEmpty()) {
            res = "Pas de propriétaires"
        } else {
            res += "Propriétaires\n"
            owners.forEach { owner ->
                res += owner.name + "\n"
            }
        }

        return res
    }

    @Ignore
    fun getTenantConcatenate(): String {
        var res = "";

        if (tenants.isEmpty()) {
            res = "Pas de locataires"
        } else {
            res += "Locataires\n"
            tenants.forEach { tenant ->
                res += tenant.name + "\n"
            }
        }

        return res
    }

    @Ignore
    fun getContractorsConcatenate(): String {
        var res = "";

        if (contractors.isEmpty()) {
            res = "Pas de mandataires"
        } else {
            owners.forEach { contractor ->
                res += contractor.name + "\n"
            }
        }

        return res
    }

    @Ignore
    fun getPropertyAddressConcatenate(): String {
        var res = "";

        if (properties.isEmpty()) {
            res = "Pas de biens"
        } else {
            properties.forEach { property ->
                res += property.address + "\n"
            }
        }

        return res
    }

    @Ignore
    fun getPropertyTypeAndNatureConcatenate(): String {
        var res = "";

        if (properties.isEmpty()) {
            res = "Pas de biens"
        } else {
            properties.forEach { property ->
                res += property.type + " " + property.nature + "\n"
            }
        }

        return res
    }
}
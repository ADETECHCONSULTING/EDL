package fr.atraore.edl.data.models

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import fr.atraore.edl.data.models.crossRef.*
import java.io.Serializable

data class ConstatWithDetails(
    @Embedded val constat: Constat,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "ownerId",
        associateBy = Junction(ConstatOwnerCrossRef::class)
    )
    val owners: MutableList<Owner>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "tenantId",
        associateBy = Junction(ConstatTenantCrossRef::class)
    )
    val tenants: MutableList<Tenant>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "contractorId",
        associateBy = Junction(ConstatContractorCrossRef::class)
    )
    val contractors: MutableList<Contractor>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "propertyId",
        associateBy = Junction(ConstatPropertyCrossRef::class)
    )
    val properties: MutableList<Property>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "agencyId",
        associateBy = Junction(ConstatAgencyCrossRef::class)
    )
    val agency: Agency?,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "userId",
        associateBy = Junction(ConstatUsersCrossRef::class)
    )
    val user: Users?,
) : Serializable {
    @Ignore
    fun getOwnersConcatenate(withPrefix: Boolean): String {
        var res = "";

        if (owners.isEmpty()) {
            res = "Pas de propriétaires"
        } else {
            if (withPrefix) {
                res += "Propriétaires\n"
            }
            owners.forEach { owner ->
                res += owner.name + "\n"
            }
        }

        return res
    }

    @Ignore
    fun getTenantConcatenate(withPrefix: Boolean): String {
        var res = "";

        if (tenants.isEmpty()) {
            res = "Pas de locataires"
        } else {
            if (withPrefix) {
                res += "Locataires\n"
            }
            tenants.forEach { tenant ->
                res += tenant.name + "\n"
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
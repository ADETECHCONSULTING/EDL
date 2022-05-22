package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import fr.atraore.edl.data.models.crossRef.*
import fr.atraore.edl.data.models.entity.*
import java.io.Serializable

data class ConstatWithDetails (
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
    @Relation(
        parentColumn = "constatId",
        entityColumn = "roomReferenceId",
        associateBy = Junction(ConstatRoomCrossRef::class)
    )
    val rooms: MutableList<RoomReference>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "lotReferenceId",
        associateBy = Junction(ConstatLotCrossRef::class)
    )
    val lots: MutableList<LotReference>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "constat_id"
    )
    val compteurs: MutableList<Compteur>,
    @Relation(
        parentColumn = "constatId",
        entityColumn = "idConstat"
    )
    val keys: MutableList<Detail>

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
package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.LOT_REFERENCE_TABLE
import java.io.Serializable

//Lot
@Entity(tableName = LOT_REFERENCE_TABLE)
data class LotReference (
    @PrimaryKey(autoGenerate = false) val lotReferenceId: Int,
    val name: String,
    var mandatory: Boolean? = false
) : Serializable {

}
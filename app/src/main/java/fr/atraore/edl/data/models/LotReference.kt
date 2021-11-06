package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.LOT_REFERENCE_TABLE

//Lot
@Entity(tableName = LOT_REFERENCE_TABLE)
data class LotReference (
    @PrimaryKey(autoGenerate = false) val lotReferenceId: String,
    val name: String,
    var mandatory: Boolean? = false
) {

}
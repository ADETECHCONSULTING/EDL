package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.OUTDOOR_EQUIPMNT
import java.io.Serializable

@Entity(tableName = OUTDOOR_EQUIPMNT)
data class OutdoorEquipementReference (
    val name: String,
    var actif: Boolean
) : Serializable {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.CONSTAT_KEY_TABLE
import java.io.Serializable

@Entity(tableName = CONSTAT_KEY_TABLE)
data class ConstatKey (
    @PrimaryKey(autoGenerate = false) val id: String,
    val keyId: String,
    val constatId: String,
    var descriptif: String? = "",
    var alteration: String? = "",
    val quantite: Int? = null
) : Serializable {

}
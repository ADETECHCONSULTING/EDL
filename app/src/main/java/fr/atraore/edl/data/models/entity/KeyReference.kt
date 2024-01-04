package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.KEY_TABLE
import java.io.Serializable

@Entity(tableName = KEY_TABLE)
data class KeyReference (
    override var name: String,
    var actif: Boolean
) : Serializable, BaseReference(name) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
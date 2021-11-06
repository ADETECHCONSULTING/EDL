package fr.atraore.edl.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.ELEMENT_REFERENCE_TABLE

//Element
@Entity(tableName = ELEMENT_REFERENCE_TABLE)
data class ElementReference (
    @PrimaryKey(autoGenerate = false) val elementReferenceId: String,
    val name: String,
    var mandatory: Boolean? = false
) {

}
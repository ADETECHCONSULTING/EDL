package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.PHOTO_COMPTEUR_TABLE

@Entity(tableName = PHOTO_COMPTEUR_TABLE)
data class PhotoCompteur(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo(name = "compteur_id") val compteurId: String,
    val fullpath: String
) {
}
package fr.atraore.edl.data.models.data

import androidx.room.Embedded
import androidx.room.Relation
import fr.atraore.edl.data.models.entity.Compteur
import fr.atraore.edl.data.models.entity.PhotoCompteur

data class CompteurWithPhotos (
    @Embedded
    val compteur: Compteur,
    @Relation(
        parentColumn = "id",
        entityColumn = "compteurId"
    )
    val photos: MutableList<PhotoCompteur>
)
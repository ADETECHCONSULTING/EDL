package fr.atraore.edl.data.models.entity

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import fr.atraore.edl.utils.COMPTEUR_TABLE
import java.io.Serializable

@Entity(tableName = COMPTEUR_TABLE, primaryKeys = ["constat_id", "compteur_ref_id"])
data class Compteur (
    @ColumnInfo(name = "constat_id") val constatId: String,
    @ColumnInfo(name = "compteur_ref_id") val compteurRefId: Int,
    var etat: String? = null,
    @ColumnInfo(name = "primary_quantity") var primaryQuantity: String? = null,
    @ColumnInfo(name = "secondary_quantity") var secondaryQuantity: String? = null,
    var localisation: String? = null,
    @ColumnInfo(name = "motif_non_releve") var motifNonReleve: String? = null,
    @ColumnInfo(name = "image_path") val imagePath: String? = null,
    @ColumnInfo(name = "image_path_second") val imagePathSecond: String? = null,
    var fonctionmt: Boolean? = null,
    var proprete: String? = null,
    var num: String? = null,
    var fourni: String? = null,
    var contrat: String? = null,
    var divers: String? = null,
    var comment: String? = null
) : Serializable, BaseObservable()  {

    @get:Bindable
    @Ignore
    var getEtat: String? = etat
    set(value) {
        field = value
        etat = value
        notifyPropertyChanged(BR.getEtat)
    }

    @get:Bindable
    @Ignore
    var getPrimaryQuantity: String? = primaryQuantity
        set(value) {
            field = value
            primaryQuantity = value
            notifyPropertyChanged(BR.getPrimaryQuantity)
        }

    @get:Bindable
    @Ignore
    var getSecondaryQuantity: String? = secondaryQuantity
        set(value) {
            field = value
            secondaryQuantity = value
            notifyPropertyChanged(BR.getSecondaryQuantity)
        }

    @get:Bindable
    @Ignore
    var getLocalisation: String? = localisation
        set(value) {
            field = value
            localisation = value
            notifyPropertyChanged(BR.getLocalisation)
        }

    @get:Bindable
    @Ignore
    var getMotifNonReleve: String? = motifNonReleve
        set(value) {
            field = value
            motifNonReleve = value
            notifyPropertyChanged(BR.getMotifNonReleve)
        }
}
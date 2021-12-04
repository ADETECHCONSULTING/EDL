package fr.atraore.edl.data.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import fr.atraore.edl.utils.COMPTEUR_TABLE

@Entity(tableName = COMPTEUR_TABLE, primaryKeys = ["constat_id", "compteur_ref_id"])
data class Compteur (
    @ColumnInfo(name = "constat_id") val constatId: String,
    @ColumnInfo(name = "compteur_ref_id") val compteurRefId: Int,
    var etat: Boolean? = null,
    @ColumnInfo(name = "primary_quantity") var primaryQuantity: String? = null,
    @ColumnInfo(name = "secondary_quantity") var secondaryQuantity: String? = null,
    var localisation: String? = null,
    @ColumnInfo(name = "motif_non_releve") var motifNonReleve: String? = null,
) : BaseObservable() {

    @get:Bindable
    @Ignore
    var getEtat: Boolean? = null
    set(value) {
        field = value
        etat = value
        notifyPropertyChanged(BR.getEtat)
    }

    @get:Bindable
    @Ignore
    var getPrimaryQuantity: String? = null
        set(value) {
            field = value
            primaryQuantity = value
            notifyPropertyChanged(BR.getPrimaryQuantity)
        }

    @get:Bindable
    @Ignore
    var getSecondaryQuantity: String? = null
        set(value) {
            field = value
            secondaryQuantity = value
            notifyPropertyChanged(BR.getSecondaryQuantity)
        }

    @get:Bindable
    @Ignore
    var getLocalisation: String? = null
        set(value) {
            field = value
            localisation = value
            notifyPropertyChanged(BR.getLocalisation)
        }

    @get:Bindable
    @Ignore
    var getMotifNonReleve: String? = null
        set(value) {
            field = value
            motifNonReleve = value
            notifyPropertyChanged(BR.getMotifNonReleve)
        }
}
package fr.atraore.edl.data.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.CONSTAT_TABLE
import java.io.Serializable
import java.sql.Date
import java.util.*

@Entity(tableName = CONSTAT_TABLE)
data class Constat(
    @PrimaryKey(autoGenerate = false) val constatId: String,
    @ColumnInfo(name = "type_constat") val typeConstat: String, //ESP - Entrant - Pre-etat - Sortant
    @ColumnInfo(name = "date_creation") val dateCreation: Date,
    val state: Int?
) : Serializable {

}
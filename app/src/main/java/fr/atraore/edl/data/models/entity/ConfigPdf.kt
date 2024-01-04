package fr.atraore.edl.data.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import fr.atraore.edl.utils.CONFIG_PDF_TABLE

const val PDF_ID_CONFIG = 1
@Entity(tableName = CONFIG_PDF_TABLE)
data class ConfigPdf (
    val textPreEtat: String?,
    val textPdfSignature: String?,
    val textPdfSignature2: String?,
    val textPdfSignature2bis: String?,
    val textPdfSignature3: String?,
) {
    @PrimaryKey(autoGenerate = false) var id: Int = PDF_ID_CONFIG
}
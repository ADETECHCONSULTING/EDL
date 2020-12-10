package fr.atraore.edl.data.models

const val PDF_ID_CONFIG = 1
data class ConfigPdf (
    val textPreEtat: String?,
    val textPdfSignature: String?,
    val textPdfSignature2: String?,
    val textPdfSignature2bis: String?,
    val textPdfSignature3: String?,
) {
    var id: Int = PDF_ID_CONFIG
}
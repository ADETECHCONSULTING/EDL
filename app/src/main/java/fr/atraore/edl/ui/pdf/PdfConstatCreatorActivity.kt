package fr.atraore.edl.ui.pdf

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.setMargins
import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity
import com.tejpratapsingh.pdfcreator.utils.PDFUtil.PDFUtilListener
import com.tejpratapsingh.pdfcreator.views.PDFBody
import com.tejpratapsingh.pdfcreator.views.PDFFooterView
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView
import com.tejpratapsingh.pdfcreator.views.PDFTableView
import com.tejpratapsingh.pdfcreator.views.PDFTableView.PDFTableRowView
import com.tejpratapsingh.pdfcreator.views.basic.*
import dagger.hilt.android.AndroidEntryPoint
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.data.models.entity.Detail
import fr.atraore.edl.data.models.entity.RoomReference
import fr.atraore.edl.photo.PickerConfiguration
import fr.atraore.edl.utils.COMPTEUR_LABELS
import fr.atraore.edl.utils.InsertMedia
import kotlinx.android.synthetic.main.footer_layout.*
import kotlinx.android.synthetic.main.footer_layout.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.net.URLConnection
import java.util.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class PdfConstatCreatorActivity : PDFCreatorActivity(), CoroutineScope {
    private val TAG = PdfConstatCreatorActivity::class.simpleName

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val viewModel: PdfConstatCreatorViewModel by viewModels()
    private lateinit var constat: ConstatWithDetails
    private var roomWithDetails: Map<RoomReference, List<Detail>>? = null
    private lateinit var emptySpace: PDFLineSeparatorView
    private lateinit var constatId: String
    private lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        constatId = intent.getStringExtra("constatId").orEmpty()

        if (constatId.isNotEmpty()) {
            viewModel.constatAndRoomDetailsCombined(constatId).observe(this) {
                it.let { pair ->

                    pair.first?.let { constatWithDetails ->
                        this.constat = constatWithDetails
                        this.roomWithDetails = pair.second

                        launch {
                            createPDF(constatId, object : PDFUtilListener {
                                override fun pdfGenerationSuccess(savedPDFFile: File) {
                                    file = savedPDFFile
                                    Toast.makeText(this@PdfConstatCreatorActivity, "PDF Created", Toast.LENGTH_SHORT).show()
                                }

                                override fun pdfGenerationFailure(exception: Exception) {
                                    Log.e("PDFConstatCreator", "pdfGenerationFailure: ${exception.message}")
                                    Toast.makeText(this@PdfConstatCreatorActivity, "PDF NOT Created", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                }
            }
        }

    }

    override fun getHeaderView(pageIndex: Int): PDFHeaderView {
        val headerView = PDFHeaderView(applicationContext)
        val horizontalView = PDFHorizontalView(applicationContext)
        val pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.HEADER)
        val word = SpannableString("")
        word.setSpan(ForegroundColorSpan(Color.DKGRAY), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        pdfTextView.text = word
        pdfTextView.setLayout(
            LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT, 1F
            )
        )
        pdfTextView.view.gravity = Gravity.CENTER_VERTICAL
        pdfTextView.view.setTypeface(pdfTextView.view.typeface, Typeface.BOLD)
        horizontalView.addView(pdfTextView)
        headerView.addView(horizontalView)
        val lineSeparatorView1 = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        headerView.addView(lineSeparatorView1)
        return headerView
    }

    override fun getBodyViews(): PDFBody {
        val pdfBody = PDFBody()

        identityArea(pdfBody)
        compteurArea(pdfBody)
        keysArea(pdfBody)
        detailsArea(pdfBody)
        signatureArea(pdfBody)

        return pdfBody
    }

    override fun getFooterView(pageIndex: Int): PDFFooterView {
        val footerView = PDFFooterView(applicationContext)

        val footerViewLayout: View = LayoutInflater.from(this).inflate(R.layout.footer_layout, ctn_footer, false)
        if (constat.constat.paraphPath !== null) {
            val bitmapParaph = getBitmapFromUri(Uri.parse(constat.constat.paraphPath))

            footerViewLayout.imv_paraph.setImageBitmap(bitmapParaph)
        }
        footerViewLayout.textView.text = String.format(Locale.getDefault(), "Page: %d", pageIndex + 1)
        footerView.view.addView(footerViewLayout)

        return footerView

    }

    override fun onNextClicked(savedPDFFile: File) {
        sharedPdf()
    }


    //body
    private fun identityArea(pdfBody: PDFBody) {
        //Adresse
        val pdfCompanyNameView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H2)
        pdfCompanyNameView.setText(this.constat.agency?.name ?: "")
        pdfBody.addView(pdfCompanyNameView)
        val lineSeparatorView1 = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        pdfBody.addView(lineSeparatorView1)
        val pdfAddressView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfAddressView.setText("${this.constat.agency?.address}")
        pdfBody.addView(pdfAddressView)
        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                30, 0F
            )
        )
        pdfBody.addView(emptySpace)

        //tableau info proprio / locataire
        val widthPercentUpperInfo = intArrayOf(50, 50) // Sum should be equal to 100%

        //header vide
        val tableUpperInfo = PDFTableRowView(applicationContext)

        //Premiere ligne doit être généré en amont
        val tableRowViewUpperInfo1 = PDFTableRowView(applicationContext)
        var pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Propriétaire(s) : ${constat.getOwnersConcatenate(false)}")
        tableRowViewUpperInfo1.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Locataire(s) : ${constat.getTenantConcatenate(false)}")
        tableRowViewUpperInfo1.addToRow(pdfTextViewUpperInfo)

        //configuration de la table
        val tableViewUpperInfo = PDFTableView(applicationContext, tableUpperInfo, tableRowViewUpperInfo1)
        var tableRowViewUpperInfo = PDFTableRowView(applicationContext)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Effectué par : ${constat.user?.name}")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        tableViewUpperInfo.addRow(tableRowViewUpperInfo)
        tableRowViewUpperInfo = PDFTableRowView(applicationContext)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Mandataire : ${constat.getContractorConcatenate(false)}")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Nouvelle(s) adresse(s) : ${constat.getPropertyAddressConcatenate()}")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        tableViewUpperInfo.addRow(tableRowViewUpperInfo)
        tableRowViewUpperInfo = PDFTableRowView(applicationContext)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Adresse : ")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Date d'entrée : ${constat.constat.dateCreationFormatted}")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        tableViewUpperInfo.addRow(tableRowViewUpperInfo)
        tableViewUpperInfo.setColumnWidth(*widthPercentUpperInfo)
        pdfBody.addView(tableViewUpperInfo)
        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                30, 0F
            )
        )
        pdfBody.addView(emptySpace)

        //tableau info bien
        val widthPercentProperty = intArrayOf(50, 50) // Sum should be equal to 100%

        //header vide
        val tableProperty = PDFTableRowView(applicationContext)

        //Premiere ligne doit être généré en amont
        val tableRowViewProperty1 = PDFTableRowView(applicationContext)
        var pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Type(s) : ${constat.getPropertyTypeAndNatureConcatenate()}")
        tableRowViewProperty1.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Etage(s) : ${constat.properties.map { property -> property.floor }.joinToString(", ")}")
        tableRowViewProperty1.addToRow(pdfTextViewProperty)

        //configuration de la table
        val tableViewProperty = PDFTableView(applicationContext, tableProperty, tableRowViewProperty1)
        var tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Adresse : ${constat.getPropertyAddressConcatenate()}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Escalier(s) : ${constat.properties.map { property -> property.stairCase }.joinToString(", ")}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Porte(s) : ${constat.properties.map { property -> property.appartmentDoor }.joinToString(", ")}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Cave(s) : ${constat.properties.map { property -> property.caveDoor }.joinToString(", ")}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Grenier(s) : ${constat.properties.map { property -> property.atticDoor }.joinToString(", ")}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Parking(s) : ${constat.properties.map { property -> property.parkingDoor }.joinToString(", ")}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Box(s) : ${constat.properties.map { property -> property.boxDoor }.joinToString(", ")}")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableViewProperty.setColumnWidth(*widthPercentProperty)
        pdfBody.addView(tableViewProperty)
    }

    private fun compteurArea(pdfBody: PDFBody) {


        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                30, 0F
            )
        )
        pdfBody.addView(emptySpace)

        //tableau compteur 1
        var widthPercent = intArrayOf(20, 10, 10, 20, 40) // Sum should be equal to 100%
        val headersCompteur1 = arrayOf("Equipement", "Etat", "Fonct.", "Propreté", "Commentaire")
        var pdfTableTitleView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H3)
        pdfTableTitleView.setText("Relevé des compteurs 1/2")
        pdfTableTitleView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        pdfBody.addView(pdfTableTitleView)
        var tableHeader = PDFTableRowView(applicationContext)
        for (s in headersCompteur1) {
            val pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(s)
            tableHeader.addToRow(pdfTextView)
        }
        var tableRowView1 = PDFTableRowView(applicationContext)
        var tableView = PDFTableView(applicationContext, tableHeader, tableRowView1)

        constat.compteurs.forEach { compteur ->
            val tableRowView = PDFTableRowView(applicationContext)
            var pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(COMPTEUR_LABELS[compteur.compteurRefId - 1])
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.etat == null) "" else compteur.etat)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.fonctionmt == null) "NON" else compteur.fonctionmt.toString())
            if (pdfTextView.text.toString().equals("non", true)) {
                pdfTextView.setBackgroundColor(Color.RED)
                pdfTextView.setTextColor(Color.WHITE)
            } else if (pdfTextView.text.toString().equals("oui", true)) {
                pdfTextView.setBackgroundColor(Color.GREEN)
                pdfTextView.setTextColor(Color.WHITE)
            }
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.proprete == null) "" else compteur.proprete)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.comment == null) "" else compteur.comment)
            tableRowView.addToRow(pdfTextView)
            tableView.addRow(tableRowView)
        }

        tableView.setColumnWidth(*widthPercent)
        pdfBody.addView(tableView)

        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                30, 0F
            )
        )
        pdfBody.addView(emptySpace)

        //tableau compteur 2
        widthPercent = intArrayOf(30, 10, 10, 10, 10, 15, 15) // Sum should be equal to 100%
        val headersCompteur2 = arrayOf("Equipement", "Index 1", "Index 2", "N°", "Fourn.", "Contrat", "Divers")
        pdfTableTitleView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H3)
        pdfTableTitleView.setText("Relevé des compteurs 2/2")
        pdfTableTitleView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        pdfBody.addView(pdfTableTitleView)
        tableHeader = PDFTableRowView(applicationContext)
        for (s in headersCompteur2) {
            val pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(s)
            tableHeader.addToRow(pdfTextView)
        }
        tableRowView1 = PDFTableRowView(applicationContext)
        tableView = PDFTableView(applicationContext, tableHeader, tableRowView1)

        constat.compteurs.forEach { compteur ->
            val tableRowView = PDFTableRowView(applicationContext)
            var pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(COMPTEUR_LABELS[compteur.compteurRefId - 1])
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.getPrimaryQuantity == null) "" else compteur.getPrimaryQuantity)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.getSecondaryQuantity == null) "" else compteur.secondaryQuantity)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.num == null) "" else compteur.num)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.fourni == null) "" else compteur.fourni)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.contrat == null) "" else compteur.contrat)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.divers == null) "" else compteur.divers)
            tableRowView.addToRow(pdfTextView)
            tableView.addRow(tableRowView)
        }

        tableView.setColumnWidth(*widthPercent)
        pdfBody.addView(tableView)

        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                30, 0F
            )
        )
        pdfBody.addView(emptySpace)

        //images
        val horizontalView = PDFHorizontalView(applicationContext)

        constat.compteurs.forEach { compteur ->
            compteur.imagePath?.let {
                val bitmap = getBitmapFromUri(Uri.parse(compteur.imagePath))
                addImageInView(bitmap, horizontalView, COMPTEUR_LABELS[compteur.compteurRefId - 1])
            }
            compteur.imagePathSecond?.let {
                val bitmap = getBitmapFromUri(Uri.parse(compteur.imagePathSecond))
                addImageInView(bitmap, horizontalView, COMPTEUR_LABELS[compteur.compteurRefId - 1])
            }
        }

        pdfBody.addView(horizontalView)
    }

    private fun keysArea(pdfBody: PDFBody) {

        //tableau compteur 1
        val widthPercent = intArrayOf(20, 20, 10, 10, 10, 30) // Sum should be equal to 100%
        val headersCompteur1 = arrayOf("Equipement", "Nature", "Etat", "Fonct.", "Propreté", "Commentaire")
        val pdfTableTitleView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H3)
        pdfTableTitleView.setText("Liste des clefs")
        pdfTableTitleView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        pdfBody.addView(pdfTableTitleView)

        val tableHeader = PDFTableRowView(applicationContext)
        for (s in headersCompteur1) {
            val pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(s)
            tableHeader.addToRow(pdfTextView)
        }
        val tableRowView1 = PDFTableRowView(applicationContext)
        val tableView = PDFTableView(applicationContext, tableHeader, tableRowView1)

        constat.keys.filter { detail -> detail.idKey != null }.forEach { key ->
            val tableRowView = PDFTableRowView(applicationContext)
            var pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(key.intitule)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (key.nature == null) "" else key.nature)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (key.etat == null) "" else key.etat)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (key.fonctionmt == null) "NON" else key.fonctionmt.toString())
            if (pdfTextView.text.toString().equals("non", true)) {
                pdfTextView.setBackgroundColor(Color.RED)
                pdfTextView.setTextColor(Color.WHITE)
            } else if (pdfTextView.text.toString().equals("oui", true)) {
                pdfTextView.setBackgroundColor(Color.GREEN)
                pdfTextView.setTextColor(Color.WHITE)
            }
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (key.proprete == null) "" else key.proprete)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (key.notes == null) "" else key.notes)
            tableRowView.addToRow(pdfTextView)
            tableView.addRow(tableRowView)
        }

        tableView.setColumnWidth(*widthPercent)
        pdfBody.addView(tableView)

        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                30, 0F
            )
        )
        pdfBody.addView(emptySpace)

        //images
        val horizontalView = PDFHorizontalView(applicationContext)

        constat.keys.forEach { key ->
            key.imagePath?.let {
                val bitmap = getBitmapFromUri(Uri.parse(key.imagePath))
                addImageInView(bitmap, horizontalView, key.intitule)
            }
            key.imagePathSecond?.let {
                val bitmap = getBitmapFromUri(Uri.parse(key.imagePathSecond))
                addImageInView(bitmap, horizontalView, key.intitule)
            }
        }

        pdfBody.addView(horizontalView)
    }

    private fun detailsArea(pdfBody: PDFBody) {

        roomWithDetails?.let { roomWithDetails ->
            for ((room, values) in roomWithDetails) {
                //tableau compteur 1
                val widthPercent = intArrayOf(20, 20, 10, 10, 10, 30) // Sum should be equal to 100%
                val headersCompteur1 = arrayOf("Equipement", "Nature", "Etat", "Fonct.", "Propreté", "Commentaire")
                val pdfTableTitleView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H3)
                pdfTableTitleView.setText(room.name)
                pdfTableTitleView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
                pdfBody.addView(pdfTableTitleView)

                val tableHeader = PDFTableRowView(applicationContext)
                for (s in headersCompteur1) {
                    val pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(s)
                    tableHeader.addToRow(pdfTextView)
                }
                val tableRowView1 = PDFTableRowView(applicationContext)
                val tableView = PDFTableView(applicationContext, tableHeader, tableRowView1)

                values.forEach { key ->
                    val tableRowView = PDFTableRowView(applicationContext)
                    var pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(key.intitule)
                    tableRowView.addToRow(pdfTextView)
                    pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(if (key.nature == null) "" else key.nature)
                    tableRowView.addToRow(pdfTextView)
                    pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(if (key.etat == null) "" else key.etat)
                    tableRowView.addToRow(pdfTextView)
                    pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(if (key.fonctionmt == null) "NON" else key.fonctionmt.toString())
                    if (pdfTextView.text.toString().equals("non", true)) {
                        pdfTextView.setBackgroundColor(Color.RED)
                        pdfTextView.setTextColor(Color.WHITE)
                    } else if (pdfTextView.text.toString().equals("oui", true)) {
                        pdfTextView.setBackgroundColor(Color.GREEN)
                        pdfTextView.setTextColor(Color.WHITE)
                    }
                    tableRowView.addToRow(pdfTextView)
                    pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(if (key.proprete == null) "" else key.proprete)
                    tableRowView.addToRow(pdfTextView)
                    pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                    pdfTextView.setText(if (key.notes == null) "" else key.notes)
                    tableRowView.addToRow(pdfTextView)
                    tableView.addRow(tableRowView)
                }

                tableView.setColumnWidth(*widthPercent)
                pdfBody.addView(tableView)

                emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
                emptySpace.setLayout(
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        30, 0F
                    )
                )
                pdfBody.addView(emptySpace)

                //images
                val horizontalView = PDFHorizontalView(applicationContext)

                values.forEach { key ->
                    key.imagePath?.let {
                        val bitmap = getBitmapFromUri(Uri.parse(key.imagePath))
                        addImageInView(bitmap, horizontalView, key.intitule)
                    }
                    key.imagePathSecond?.let {
                        val bitmap = getBitmapFromUri(Uri.parse(key.imagePathSecond))
                        addImageInView(bitmap, horizontalView, key.intitule)
                    }
                }

                pdfBody.addView(horizontalView)
            }
        }
    }

    private fun signatureArea(pdfBody: PDFBody) {

        val pdfSignatureTitle = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H3)
        pdfSignatureTitle.setText("Signatures")
        pdfSignatureTitle.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        pdfBody.addView(pdfSignatureTitle)

        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                10, 0F
            )
        )
        pdfBody.addView(emptySpace)

        var pdfSignatureContent = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfSignatureContent.setText(
            "Les soussignés reconnaissent exactes les constatations sur l'état du logement, " +
                    "et reconnaissent avoir reçu chacun l'ensemble des élements leur permettant de récupérer un exemplaire" +
                    " du présent état des lieux et s'accordent pour y faire référence."
        )
        pdfSignatureContent.setTextColor(ContextCompat.getColor(this, R.color.black))
        pdfBody.addView(pdfSignatureContent)

        emptySpace = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        emptySpace.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                10, 0F
            )
        )
        pdfBody.addView(emptySpace)

        pdfSignatureContent = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfSignatureContent.setText("Le présent état des lieux, a été établi contradictoirement entre les parties qui le reconnaissent exact.")
        pdfSignatureContent.setTextColor(ContextCompat.getColor(this, R.color.black))
        pdfBody.addView(pdfSignatureContent)

        if (constat.constat.onwerSignaturePath !== null && constat.constat.tenantSignaturePath !== null) {

            val horizontalView = PDFHorizontalView(applicationContext)

            val bitmapOwner = getBitmapFromUri(Uri.parse(constat.constat.onwerSignaturePath))
            val bitmapTenant = getBitmapFromUri(Uri.parse(constat.constat.tenantSignaturePath))
            val targetBmpOwner = bitmapOwner?.let { bitmapResizer(it.copy(Bitmap.Config.ARGB_8888, false), 120, 120) }
            val targetBmpTenant = bitmapTenant?.let { bitmapResizer(it.copy(Bitmap.Config.ARGB_8888, false), 120, 120) }

            if (targetBmpOwner !== null) {
                val verticalView = PDFVerticalView(applicationContext)

                val pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                pdfTextViewProperty.setText("Présent document transmis et accepté par le(s) locataire(s) sortant : ${constat.getOwnersConcatenate(false)}")
                verticalView.addView(pdfTextViewProperty)

                val pdfImageView = PDFImageView(applicationContext).setImageBitmap(targetBmpOwner)
                verticalView.addView(pdfImageView)

                //FIX quand il y a plusieurs éléments dans le layout lineaire
                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(10)
                params.weight = 1f
                verticalView.view.layoutParams = params

                horizontalView.addView(verticalView)
            } else {
                Log.d(TAG, "signatureArea: Le resize de la signature du propriétaire n'a pas fonctionné")
            }

            if (targetBmpTenant !== null) {
                val verticalView = PDFVerticalView(applicationContext)

                val pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                pdfTextViewProperty.setText("Présent document transmis et accepté par le(s) locataire(s) sortant : ${constat.getTenantConcatenate(false)}")
                verticalView.addView(pdfTextViewProperty)

                val pdfImageView = PDFImageView(applicationContext).setImageBitmap(targetBmpTenant)
                verticalView.addView(pdfImageView)

                val params = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(10)
                params.weight = 1f
                verticalView.view.layoutParams = params

                horizontalView.addView(verticalView)
            } else {
                Log.d(TAG, "signatureArea: Le resize de la signature du locataire n'a pas fonctionné")
            }

            pdfBody.addView(horizontalView)
        } else {
            Log.d(TAG, "signatureArea: la signature du propriétaire ou du locataire n'a pas été renseignée")
        }

    }

    private fun addImageInView(bitmap: Bitmap?, horizontalView: PDFHorizontalView, text: String) {
        val verticalView = PDFVerticalView(applicationContext)

        if (bitmap !== null) {
            val targetBmp = Bitmap.createScaledBitmap(bitmap, 120, 120, false)

            if (targetBmp !== null) {
                val pdfImageView = PDFImageView(applicationContext)
                    .setImageBitmap(targetBmp)
                verticalView.addView(pdfImageView)
                val pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                pdfTextViewProperty.setText(text)
                verticalView.addView(pdfTextViewProperty)

                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params.setMargins(10)
                verticalView.view.layoutParams = params
                horizontalView.addView(verticalView)
            }
        }
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            InsertMedia.loadPhotoFromInternalStorage(this, imageUri.toString())
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
    }

    private fun bitmapResizer(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(bitmap, middleX - bitmap.width / 2, middleY - bitmap.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
        return scaledBitmap
    }

    fun getResizedBitmap(bm: Bitmap, newHeight: Int, newWidth: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // create a matrix for the manipulation
        val matrix = Matrix()
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)
        // recreate the new Bitmap
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false)
    }

    fun sharedPdf() {
        if (file == null || !file.exists()) {
            Toast.makeText(this, "une erreur est survenue lors du partage du PDF", Toast.LENGTH_SHORT).show()
        }
        val intentShareFile = Intent(Intent.ACTION_SEND)
        val apkURI: Uri = FileProvider.getUriForFile(
            this, PickerConfiguration.getAuthority(), file
        )
        intentShareFile.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(file.name))
        intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intentShareFile.putExtra(
            Intent.EXTRA_STREAM,
            apkURI
        )
        startActivity(Intent.createChooser(intentShareFile, "Share File"))
    }


}
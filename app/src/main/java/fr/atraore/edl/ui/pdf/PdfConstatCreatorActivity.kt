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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import fr.atraore.edl.utils.COMPTEUR_LABELS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import kotlin.coroutines.CoroutineContext


@AndroidEntryPoint
class PdfConstatCreatorActivity : PDFCreatorActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val viewModel: PdfConstatCreatorViewModel by viewModels()
    private lateinit var constat: ConstatWithDetails
    private lateinit var emptySpace: PDFLineSeparatorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        //intent.getStringExtra("constatId")?.let {
            viewModel.constatDetail("51bf6775-aa30-4d30-9fa1-6ee34230c541").observe(this) {
                it?.let { constatWithDetails ->
                    this.constat = constatWithDetails

                    launch {
                        createPDF("test", object : PDFUtilListener {
                            override fun pdfGenerationSuccess(savedPDFFile: File) {
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
        //}

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


        return pdfBody
    }

    override fun getFooterView(pageIndex: Int): PDFFooterView {
        val footerView = PDFFooterView(applicationContext)
        val pdfTextViewPage = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.SMALL)
        pdfTextViewPage.setText(String.format(Locale.getDefault(), "Page: %d", pageIndex + 1))
        pdfTextViewPage.setLayout(
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 0F
            )
        )
        pdfTextViewPage.view.gravity = Gravity.CENTER_HORIZONTAL
        footerView.addView(pdfTextViewPage)
        return footerView
    }

    override fun onNextClicked(savedPDFFile: File) {
        val pdfUri = Uri.fromFile(savedPDFFile)
        val intentPdfViewer = Intent(this@PdfConstatCreatorActivity, PdfViewerExampleActivity::class.java)
        intentPdfViewer.putExtra(PdfViewerExampleActivity.PDF_FILE_URI, pdfUri)
        startActivity(intentPdfViewer)
    }



    //body
    private fun identityArea(pdfBody: PDFBody) {
        //Adresse
        val pdfCompanyNameView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.H2)
        pdfCompanyNameView.setText("Dom'Services")
        pdfBody.addView(pdfCompanyNameView)
        val lineSeparatorView1 = PDFLineSeparatorView(applicationContext).setBackgroundColor(Color.WHITE)
        pdfBody.addView(lineSeparatorView1)
        val pdfAddressView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfAddressView.setText("3 rue favernay\n74160 saint julien en genevois")
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
        pdfTextViewUpperInfo.setText("Propriétaire : M. FRANKEN Loïc")
        tableRowViewUpperInfo1.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Locataire : M. FRANKEN Loïc")
        tableRowViewUpperInfo1.addToRow(pdfTextViewUpperInfo)

        //configuration de la table
        val tableViewUpperInfo = PDFTableView(applicationContext, tableUpperInfo, tableRowViewUpperInfo1)
        var tableRowViewUpperInfo = PDFTableRowView(applicationContext)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Effectué par : Mr Dom Germain")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        tableViewUpperInfo.addRow(tableRowViewUpperInfo)
        tableRowViewUpperInfo = PDFTableRowView(applicationContext)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Mandataire : Franken Loic")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Nouvelle(s) adresse(s) : 20 place de l'aviateur")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        tableViewUpperInfo.addRow(tableRowViewUpperInfo)
        tableRowViewUpperInfo = PDFTableRowView(applicationContext)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Adresse : ")
        tableRowViewUpperInfo.addToRow(pdfTextViewUpperInfo)
        pdfTextViewUpperInfo = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewUpperInfo.setText("Date d'entrée : 15 mai 2022")
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
    }

    private fun compteurArea(pdfBody: PDFBody) {

        //tableau info bien
        val widthPercentProperty = intArrayOf(50, 50) // Sum should be equal to 100%

        //header vide
        val tableProperty = PDFTableRowView(applicationContext)

        //Premiere ligne doit être généré en amont
        val tableRowViewProperty1 = PDFTableRowView(applicationContext)
        var pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Type : Appart T2 sur 1 niveau")
        tableRowViewProperty1.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Etage : 3")
        tableRowViewProperty1.addToRow(pdfTextViewProperty)

        //configuration de la table
        val tableViewProperty = PDFTableView(applicationContext, tableProperty, tableRowViewProperty1)
        var tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Adresse : 3 rue de l'industrie\n74160 saint julien en genevois")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Escalier : ")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Porte : ")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Cave : ")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Grenier : ")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Parking : ")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableRowViewProperty = PDFTableRowView(applicationContext)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
        pdfTextViewProperty.setText("Box : ")
        tableRowViewProperty.addToRow(pdfTextViewProperty)
        tableViewProperty.addRow(tableRowViewProperty)
        tableViewProperty.setColumnWidth(*widthPercentProperty)
        pdfBody.addView(tableViewProperty)

        //tableau compteur 1
        var widthPercent = intArrayOf(20, 20, 10, 10, 10, 30) // Sum should be equal to 100%
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
            pdfTextView.setText(COMPTEUR_LABELS[compteur.compteurRefId-1])
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.etat == null) "" else compteur.etat)
            tableRowView.addToRow(pdfTextView)
            pdfTextView = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
            pdfTextView.setText(if (compteur.fonctionmt == null) "NON" else compteur.fonctionmt.toString())
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
            pdfTextView.setText(COMPTEUR_LABELS[compteur.compteurRefId-1])
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
                addImageInView(bitmap, horizontalView, pdfBody, COMPTEUR_LABELS[compteur.compteurRefId-1])
            }
            compteur.imagePathSecond?.let {
                val bitmap = getBitmapFromUri(Uri.parse(compteur.imagePathSecond))
                addImageInView(bitmap, horizontalView, pdfBody, COMPTEUR_LABELS[compteur.compteurRefId-1])
            }
        }

        pdfBody.addView(horizontalView)
    }

    private fun addImageInView(bitmap: Bitmap?, horizontalView: PDFHorizontalView, pdfBody: PDFBody, text: String) {
        val verticalView = PDFVerticalView(applicationContext)

        if (bitmap !== null) {
            val targetBmp = bitmapResizer(bitmap.copy(Bitmap.Config.ARGB_8888, false), 120, 120)

            if (targetBmp !== null) {
                val pdfImageView = PDFImageView(applicationContext)
                    .setImageBitmap(targetBmp)
                verticalView.addView(pdfImageView)
                val pdfTextViewProperty = PDFTextView(applicationContext, PDFTextView.PDF_TEXT_SIZE.P)
                pdfTextViewProperty.setText(text)
                verticalView.addView(pdfTextViewProperty)

                horizontalView.addView(verticalView)
            }
        }
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, imageUri))
        } else {
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
    }

    fun bitmapResizer(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
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

}
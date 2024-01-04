package fr.atraore.edl.ui.pdf

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.view.*
import fr.atraore.edl.R
import fr.atraore.edl.data.models.data.ConstatWithDetails
import fr.atraore.edl.ui.edl.BaseFragment
import fr.atraore.edl.utils.ARGS_CONSTAT
import fr.atraore.edl.utils.PDF_GENERATOR
import java.io.File
import java.io.FileOutputStream


class PdfGeneratorFragment() : BaseFragment(PDF_GENERATOR) {
    private val TAG = PdfGeneratorFragment::class.simpleName
    private val WIDTH = 595
    private val HEIGHT = 842
    private lateinit var pdfRenderer: PdfRenderer
    private lateinit var currentPage: PdfRenderer.Page

    override val title: String
        get() = PDF_GENERATOR

    override fun goNext() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_compteur)?.isVisible = false
        menu.findItem(R.id.action_add_room)?.isVisible = true
        menu.findItem(R.id.action_next)?.isVisible = false
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_room -> {
                createPdf()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pdf_generator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val constat = arguments?.getSerializable(ARGS_CONSTAT) as ConstatWithDetails
    }

    private fun createPdf() {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_pdf_generator, null)

        //Fetch the dimensions of the viewport
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context?.display?.getRealMetrics(displayMetrics)
            displayMetrics.densityDpi
        }
        else{
            activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        }
        view.measure(
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.widthPixels, View.MeasureSpec.EXACTLY
            ),
            View.MeasureSpec.makeMeasureSpec(
                displayMetrics.heightPixels, View.MeasureSpec.EXACTLY
            )
        )

        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true)
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(WIDTH, HEIGHT, 1).create()

        val page = pdfDocument.startPage(pageInfo)
        page.canvas.drawBitmap(bitmap, 0F, 0F, null)
        pdfDocument.finishPage(page)

        val filePath = File(context?.getExternalFilesDir(null), "bitmapPdf.pdf")
        pdfDocument.writeTo(FileOutputStream(filePath))

        pdfDocument.close()
        openPdf(view)
    }

    private fun openPdf(view: View) {
        context?.let { context ->
            val file = File(context.getExternalFilesDir(null), "bitmapPdf.pdf")
            if (file.exists()) {
                val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
                pdfRenderer = PdfRenderer(pfd)
                currentPage = pdfRenderer.openPage(0)
                currentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            }
        }
    }

    override fun onStop() {
        super.onStop()
        currentPage.close()
        pdfRenderer.close()
    }
}
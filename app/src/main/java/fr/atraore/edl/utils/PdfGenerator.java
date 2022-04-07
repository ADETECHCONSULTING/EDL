package fr.atraore.edl.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;

import fr.atraore.edl.data.models.data.ConstatWithDetails;

public class PdfGenerator {

    public static void createPdf(ConstatWithDetails constatWithDetails) {
        if (constatWithDetails != null) {
            PdfDocument pdfDocument = new PdfDocument();
            Paint paint = new Paint();

            PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(1200, 2010, 1).create();
            PdfDocument.Page page1 = pdfDocument.startPage(myPageInfo);
            Canvas canvas = page1.getCanvas();
        }
    }

}

/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */

package com.itextpdf.samples.sandbox.typography.odia;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.licensekey.LicenseKey;

import java.io.File;
import java.io.IOException;

public class OdiaTable {

    public static final String DEST = "./target/sandbox/typography/OdiaTable.pdf";
    public static final String FONTS_FOLDER = "./src/test/resources/font/";

    public static void main(String[] args) throws Exception {

        // Load the license file to use typography features
        LicenseKey.loadLicenseFile(System.getenv("ITEXT7_LICENSEKEY") + "/itextkey-typography.xml");

        File file = new File(DEST);
        file.getParentFile().mkdirs();

        new OdiaTable().createPDF(DEST);
    }

    public void createPDF(String dest) throws IOException {

        // Create a pdf document along with a Document (default root layout element) instance
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(dest));
        Document document = new Document(pdfDocument);

        // ସବୁ ମନୁଷ୍ଯ ଜନ୍ମକାଳରୁ
        String text = "\u0B38\u0B2C\u0B41\u0020\u0B2E\u0B28\u0B41\u0B37\u0B4D\u0B2F\u0020\u0B1C\u0B28\u0B4D\u0B2E\u0B15" +
                "\u0B3E\u0B33\u0B30\u0B41";

        PdfFont font = PdfFontFactory.createFont(FONTS_FOLDER + "NotoSansOriya-Regular.ttf",
                PdfEncodings.IDENTITY_H);

        // Overwrite some default document font-related properties. From now on they will be used for all the elements
        // added to the document unless they are overwritten inside these elements
        document
                .setFont(font)
                .setFontSize(10);

        Table table = new Table(UnitValue.createPercentArray(3)).useAllAvailableWidth();
        table
                .addCell(text)
                .addCell(text)
                .addCell(text);

        document.add(table);

        document.close();
    }
}

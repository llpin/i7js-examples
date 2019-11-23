/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    For more information, please contact iText Software at this address:
    sales@itextpdf.com
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

import java.io.File;
import java.io.IOException;

public class TableInColumn {
    public static final String DEST = "./target/sandbox/objects/table_in_column.pdf";

    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableInColumn().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Cell cell = new Cell();
        for (int i = 1; i <= 20; i++) {
            cell.add(new Paragraph("Line " + i));
        }

        Table table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.addCell(cell);
        table.setNextRenderer(new CustomTableRenderer(table, resolveTableRect(doc, table)));
        doc.add(table);

        doc.add(new AreaBreak());
        table = new Table(UnitValue.createPercentArray(1)).useAllAvailableWidth();
        table.addCell(cell.clone(true));
        table.addCell(cell.clone(true));
        table.setNextRenderer(new CustomTableRenderer(table, resolveTableRect(doc, table)));
        doc.add(table);

        doc.close();
    }

    private static Rectangle resolveTableRect(Document doc, Table table) {
        Rectangle pageDimension = new Rectangle(36, 36, 523, 770);
        IRenderer tableRenderer = table.createRendererSubTree().setParent(doc.getRenderer());
        LayoutResult tableLayoutResult = tableRenderer.layout(new LayoutContext(new LayoutArea(0, pageDimension)));

        Rectangle resultRect;
        if (LayoutResult.PARTIAL == tableLayoutResult.getStatus()) {
            resultRect = pageDimension;
        } else {
            Rectangle tableBBox = tableLayoutResult.getOccupiedArea().getBBox();
            resultRect = new Rectangle(pageDimension.getX(), ((tableBBox.getBottom() + pageDimension.getX()) / 2),
                    pageDimension.getWidth(), tableBBox.getHeight());
        }
        return resultRect;
    }

    protected class CustomTableRenderer extends TableRenderer {
        protected Rectangle rect;

        public CustomTableRenderer(Table modelElement, Rectangle rect) {
            super(modelElement);
            this.rect = rect;
        }

        // If renderer overflows on the next area, iText uses getNextRender() method to create a renderer for the overflow part.
        // If getNextRenderer isn't overridden, the default method will be used and thus a default rather than custom
        // renderer will be created
        @Override
        public IRenderer getNextRenderer() {
            return new CustomTableRenderer((Table) modelElement, rect);
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            return super.layout(new LayoutContext(new LayoutArea(layoutContext.getArea().getPageNumber(), rect)));
        }
    }
}

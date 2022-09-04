package com.duck.etiketi;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Service;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LabelService {

    List<LabelType> labelTypes = new ArrayList<>();

    private static final String userDir = System.getProperty("user.dir") + File.separator + "test.pdf";
    private static final String imageDir = System.getProperty("user.dir") + File.separator + "image.pdf";
    private static final String imagePngDir = System.getProperty("user.dir") + File.separator + "image.png";

    @PostConstruct
    public void setup() {
        labelTypes.add(LabelType.builder().width(198).height(120).rows(7).columns(3).build());
        labelTypes.add(LabelType.builder().width(297).height(210).rows(4).columns(2).build());

        log.info(userDir);
    }

    public File create(byte[] canvas, Integer template) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(userDir));

        pdf.addNewPage(PageSize.A4);

        LabelType labelType = labelTypes.get(template - 1);

        for (int i = 0; i < labelType.getColumns(); i++) {
            for (int j = 0; j < labelType.getRows(); j++) {
                addRect(pdf, i * labelType.getWidth(), j * labelType.getHeight(), labelType, canvas);
            }
        }

        Barcode128 barcode = new Barcode128(pdf);
        barcode.setCodeType(Barcode128.CODE128);
        barcode.setCode("12345678");
        barcode.setSize(12);
        Rectangle rect = barcode.getBarcodeSize();
        PdfFormXObject formXObject = new PdfFormXObject(new Rectangle(rect.getWidth(), rect.getHeight() + 10));
        PdfCanvas pdfCanvas = new PdfCanvas(formXObject, pdf);
        barcode.placeBarcode(pdfCanvas, Color.BLACK, Color.BLACK);
        Image bCodeImage = new Image(formXObject);
        bCodeImage.setRotationAngle(Math.toRadians(90));
        bCodeImage.setFixedPosition(0, 0);

        Document document = new Document(pdf);
        document.add(bCodeImage);

        pdf.close();

        return new File(userDir);

    }

    public File barcode(String code) throws IOException {

        PdfDocument pdf = new PdfDocument(new PdfWriter(imageDir));

        Barcode128 barcode = new Barcode128(pdf);
        barcode.setCodeType(BarcodeEAN.EAN13);
        barcode.setCode("12345678");
        barcode.setSize(9);
        Rectangle rect = barcode.getBarcodeSize();

        PageSize pageSize = new PageSize(rect);
        pdf.setDefaultPageSize(pageSize);
        pdf.addNewPage(pdf.getDefaultPageSize());

        PdfFormXObject formXObject = new PdfFormXObject(new Rectangle(rect.getWidth(), rect.getHeight()));
        PdfCanvas pdfCanvas = new PdfCanvas(formXObject, pdf);
        barcode.placeBarcode(pdfCanvas, Color.BLACK, Color.BLACK);
        Image bCodeImage = new Image(formXObject);
        // bCodeImage.setRotationAngle(Math.toRadians(90));
        bCodeImage.setFixedPosition(0, 0);

        Document document = new Document(pdf);
        document.add(bCodeImage);
        document.close();
        pdf.close();

        convertToPng();

        return new File(imagePngDir);

    }

    private void convertToPng() throws IOException {
        PDDocument document = PDDocument.load(new File(imageDir));
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

        ImageIOUtil.writeImage(bim, imagePngDir, 300);

        document.close();
    }

    private void addRect(PdfDocument pdf, int x, int y, LabelType labelType, byte[] canvas) throws IOException {

        ImageData data = ImageDataFactory.create(canvas);
        Image img = new Image(data);

        img.setWidth(labelType.getWidth());
        img.setHeight(labelType.getHeight());
        img.setFixedPosition(x, y);

        Document document = new Document(pdf);
        document.add(img);

    }

}

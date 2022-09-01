package com.duck.etiketi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LabelService {

    List<LabelType> labelTypes = new ArrayList<>();

    @PostConstruct
    public void setup() {
        labelTypes.add(LabelType.builder().width(198).height(120).rows(7).columns(3).build());

        log.info(System.getProperty("user.dir"));
    }

    public void create(byte[] canvas) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter("C:\\Users\\ivan.angelovski\\Desktop\\etiketi\\test.pdf"));

        pdf.addNewPage(PageSize.A4);

        LabelType labelType = labelTypes.get(0);

        for (int i = 0; i < labelType.getColumns(); i++) {
            for (int j = 0; j < labelType.getRows(); j++) {
                addRect(pdf, i * labelType.getWidth(), j * labelType.getHeight(), labelType, canvas);
            }
        }

        pdf.close();

    }

    private void addRect(PdfDocument pdf, int x, int y, LabelType labelType, byte[] canvas) throws IOException {

        ImageData data = ImageDataFactory.create(canvas);
        Image img = new Image(data);
        // img.setFixedPosition(position.getX(), position.getY(), position.getW(),
        // position.getH());

        img.setWidth(labelType.getWidth());
        img.setHeight(labelType.getHeight());
        img.setFixedPosition(x, y);

        Document document = new Document(pdf);
        document.add(img);

    }

}

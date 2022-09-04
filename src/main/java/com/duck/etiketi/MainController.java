package com.duck.etiketi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("main")
@CrossOrigin
@Slf4j
public class MainController {

    @Autowired
    LabelService labelService;

    @PostMapping("create")
    public String createLabel(@RequestBody List<Textbox> textboxes) throws IOException {
        textboxes.stream().forEach(e -> log.info(e.toString()));
        // labelService.create(textboxes);
        return "ok";
    }

    @PostMapping("save")
    public String uploadAvatar(@RequestParam MultipartFile canvas) throws IOException {
        byte[] bytes = canvas.getBytes();

        return labelService.create1(bytes);

    }

    @PostMapping("barcode")
    public String barcode() throws FileNotFoundException {
        labelService.barcode();

        return "ok";
    }

    @RequestMapping(path = "/download", method = RequestMethod.POST)
    public ResponseEntity<Resource> download(@RequestParam MultipartFile canvas, @RequestParam Integer template)
            throws IOException {

        byte[] bytes = canvas.getBytes();

        // File outputFile = new
        // File("C:\\Users\\ivan.angelovski\\Desktop\\etiketi\\output4File.png");
        // try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
        // outputStream.write(bytes);
        // }

        File file = labelService.create(bytes, template);
        HttpHeaders headers = new HttpHeaders();

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

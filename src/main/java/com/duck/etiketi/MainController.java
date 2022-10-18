package com.duck.etiketi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("main")
@CrossOrigin
public class MainController {

        @Autowired
        LabelService labelService;

        @GetMapping("barcode/{barcode}")
        public ResponseEntity<InputStreamResource> barcode(@PathVariable String barcode)
                        throws IOException {

                File file = labelService.barcode(barcode);

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                HttpHeaders headers = new HttpHeaders();

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentLength(file.length())
                                .contentType(MediaType.IMAGE_PNG)
                                .body(resource);

        }

        @PostMapping("download")
        public ResponseEntity<InputStreamResource> download(@RequestParam MultipartFile canvas,
                        @RequestParam Integer template, @RequestParam Integer pages)
                        throws IOException {

                byte[] bytes = canvas.getBytes();

                File file = labelService.create(bytes, template, pages);

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
                HttpHeaders headers = new HttpHeaders();

                return ResponseEntity.ok()
                                .headers(headers)
                                .contentLength(file.length())
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .body(resource);
        }

        @PostMapping("upload-image")
        public ResponseEntity<String> uploadImage(@RequestParam MultipartFile image)
                        throws IOException {

                File file = labelService.image(image.getBytes());

                return ResponseEntity.ok()
                                .headers(new HttpHeaders())
                                .body(file.getName());
        }

        @GetMapping("download-image/{code}")
        public ResponseEntity<InputStreamResource> downloadImage(@PathVariable String code)
                        throws IOException {

                File file = labelService.image(code);

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

                return ResponseEntity.ok()
                                .headers(new HttpHeaders())
                                .contentLength(file.length())
                                .contentType(MediaType.IMAGE_PNG)
                                .body(resource);

        }
}

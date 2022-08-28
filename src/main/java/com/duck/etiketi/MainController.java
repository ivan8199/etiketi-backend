package com.duck.etiketi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.websocket.Decoder.Text;

import org.springframework.asm.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        // File outputFile = new
        // File("C:\\Users\\ivan.angelovski\\Desktop\\etiketi\\outputFile.png");
        // try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
        // outputStream.write(bytes);
        // }
        labelService.create(bytes);
        return "ok";
    }
}

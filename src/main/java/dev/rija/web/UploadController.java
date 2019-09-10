package dev.rija.web;

import dev.rija.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UploadController {
    @Autowired
    StorageService storageService;

    @PostMapping("/files")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            storageService.store(file);
            return ResponseEntity.status(HttpStatus.OK).body("Upload OK " +  file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Upload KO " + file.getOriginalFilename());
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> getListFiles() {
        List<String> listOriginalFilenames = storageService.getFiles();
        List<String> fileUrls = listOriginalFilenames.stream().map(filename ->
                MvcUriComponentsBuilder.fromMethodName(UploadController.class, "getFile", filename)
                        .build().toString()).collect(Collectors.toList());

        return ResponseEntity.ok().body(fileUrls);
    }

    @GetMapping("/files/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
                .body(file);
    }

}

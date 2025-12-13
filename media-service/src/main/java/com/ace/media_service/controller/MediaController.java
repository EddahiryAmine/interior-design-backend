package com.ace.media_service.controller;

import com.ace.media_service.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final StorageService storageService;
    private final HttpServletRequest request;

    public MediaController(StorageService storageService, HttpServletRequest request) {
        this.storageService = storageService;
        this.request = request;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {

        String filename = storageService.store(file);

        String baseUrl = request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort();

        String fileUrl = baseUrl + "/media/files/" + filename;

        return ResponseEntity.ok(new UploadResponse(filename, fileUrl));
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        String contentType = "application/octet-stream";
        try {
            String detected = request.getServletContext()
                    .getMimeType(file.getFile().getAbsolutePath());
            if (detected != null) {
                contentType = detected;
            }
        } catch (IOException ignored) {}

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    private record UploadResponse(String filename, String url) {}
    @PostMapping(value = "/upload-bytes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadBytes(@RequestParam("file") MultipartFile file) {
        String filename = storageService.store(file);

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String url = baseUrl + "/media/files/" + filename;

        return ResponseEntity.ok(new UploadResponse(filename, url));
    }

}

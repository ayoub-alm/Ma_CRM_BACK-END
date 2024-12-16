package com.sales_scout.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final String IMAGE_DIRECTORY = "src/main/resources/static/images/"; // Same path as in CompanyService

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        Path imagePath = Paths.get(IMAGE_DIRECTORY + filename);
        Resource resource;

        try {
            resource = new UrlResource(imagePath.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().body(null);
        }

        if (resource.exists() && resource.isReadable()) {
            String contentType = determineContentType(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.valueOf(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private String determineContentType(String filename) {
        if (filename.endsWith(".png")) {
            return MediaType.IMAGE_PNG_VALUE;
        } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (filename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF_VALUE;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE; // Fallback for unknown types
        }
    }
}

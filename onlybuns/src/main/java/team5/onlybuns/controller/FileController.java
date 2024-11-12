package team5.onlybuns.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/uploads/{filename:.+}")
    public Resource getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("File not found");
        }
    }
}

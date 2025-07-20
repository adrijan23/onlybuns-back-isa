package team5.onlybuns.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final String UPLOAD_DIR = "uploads/";
    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/uploads/{filename:.+}")
    @Cacheable("imageCache")
    public ResponseEntity<byte[]> getFile(@PathVariable String filename) {
        long startTime = System.currentTimeMillis();

        Cache imageCache = cacheManager.getCache("imageCache");
        boolean isInCache = imageCache != null && imageCache.get(filename) != null;

        if (isInCache) {
            logger.info("🚀 CACHE HIT: Image '{}' loaded from MEMORY cache", filename);
        } else {
            logger.info("💾 CACHE MISS: Image '{}' will be loaded from DISK and cached", filename);
        }

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();

            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + filename);
            }

            byte[] imageData = Files.readAllBytes(filePath);
            long loadTime = System.currentTimeMillis() - startTime;

            if (!isInCache) {
                logger.info("📁 DISK READ: Image '{}' loaded from disk in {}ms and stored in cache", filename, loadTime);
            } else {
                logger.info("⚡ MEMORY READ: Image '{}' served from cache in {}ms", filename, loadTime);
            }

            // Determine content type based on file extension
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(imageData);

        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filename, e);
        }
    }
}

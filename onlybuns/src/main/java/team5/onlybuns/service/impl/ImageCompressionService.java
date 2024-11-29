package team5.onlybuns.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;

@Service
public class ImageCompressionService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${image.directory}")
    private String imageDirectory;

    @Value("${compression.quality}")
    private float compressionQuality;

    /**
     * Scheduled task that compresses images older than a month.
     */
    @Scheduled(cron = "${compression.cron}")
    public void compressOldImages() {
        logger.info("Starting compression of images older than one month...");

        // Ensure the directory path is correct
        File directory = new File(imageDirectory);
        if (!directory.exists() || !directory.isDirectory()) {
            logger.error("Directory not found: {}", imageDirectory);
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) return;

        Instant oneMonthAgo = Instant.now().minus(30, ChronoUnit.DAYS);

        for (File file : files) {
            if (isImageFile(file) && isOlderThanOneMonth(file, oneMonthAgo) && !isAlreadyCompressed(file)) {
                compressImage(file);
            }
        }
        logger.info("Image compression completed.");
    }

    private boolean isAlreadyCompressed(File file) {
        return file.getName().startsWith("compressed_");
    }

    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
    }

    private boolean isOlderThanOneMonth(File file, Instant oneMonthAgo) {
        return file.lastModified() < Date.from(oneMonthAgo).getTime();
    }

    /**
     * Compress a single image file.
     */
    public void compressImage(File inputFile) {
        try {
            BufferedImage image = ImageIO.read(inputFile);
            if (image == null) {
                logger.warn("Invalid image file: {}", inputFile.getAbsolutePath());
                return;
            }

            // Create compressed file with "compressed_" prefix
            File outputFile = new File(inputFile.getParent(), "compressed_" + inputFile.getName());

            try (FileOutputStream fos = new FileOutputStream(outputFile);
                 ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {

                Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
                if (!writers.hasNext()) {
                    logger.error("No available JPEG writers found");
                    return;
                }
                ImageWriter writer = writers.next();
                writer.setOutput(ios);

                ImageWriteParam param = writer.getDefaultWriteParam();
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(compressionQuality);

                writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
                writer.dispose();

                logger.info("Compressed image saved to: {}", outputFile.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Error compressing image: {}", inputFile.getName(), e);
        }
    }
}

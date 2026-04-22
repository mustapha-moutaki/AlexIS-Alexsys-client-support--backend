package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.reposiotry.AttachmentRepository;
import com.alexsysSolutions.alexsis.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocalFileStorageService implements FileStorageService {

    private AttachmentRepository attachmentRepository;

    private final String uploadDir = "uploads/";

    @Override
    public String store(MultipartFile file) {

        try {
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path path = Paths.get(uploadDir + fileName);

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("File upload failed", e);
        }
    }


    @Override
    public void delete(String fileUrl) {

        try {
            // fileUrl: http://localhost:8080/files/abc.png
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            Path path = Paths.get("uploads/" + fileName);

            Files.deleteIfExists(path);

        } catch (Exception e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }
}
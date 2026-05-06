package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.config.CloudinaryConfig;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class CloudinaryService {

    private final Logger logger = LoggerFactory.getLogger(CloudinaryService.class);

    private final Cloudinary cloudinary;
    public String upload(MultipartFile file) throws IOException{
        logger.info("File name: {}", file.getOriginalFilename());
        logger.info("File size: {}", file.getSize());
        logger.info("File type: {}", file.getContentType());
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "profiles") // optional folder
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }

    }


    // future update
    /*
    public String upload(MultipartFile file) throws IOException{
        logger.info("File name: {}", file.getOriginalFilename());
        logger.info("File size: {}", file.getSize());
        logger.info("File type: {}", file.getContentType());
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "profiles") // optional folder
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }

    }*/


}












package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.attachment.AttachmentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.attachment.AttachmentDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.dashboard.DashboardOverViewDto;
import com.alexsysSolutions.alexsis.enums.AttachmentStatus;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.AttachmentMapper;
import com.alexsysSolutions.alexsis.model.Attachment;
import com.alexsysSolutions.alexsis.reposiotry.AttachmentRepository;
import com.alexsysSolutions.alexsis.service.AttachmentService;
import com.alexsysSolutions.alexsis.service.FileStorageService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final AttachmentMapper attachmentMapper;
    private final FileStorageService fileStorageService;

    @Override
    public AttachmentDtoResponse create(AttachmentCreateDtoRequest dto) {

        if (dto.getFile() == null || dto.getFile().isEmpty()) {
            throw new ValidationException("File is required");
        }

        try {
            MultipartFile file = dto.getFile();

            //  Extract metadata
            String fileName = file.getOriginalFilename();
            String fileType = file.getContentType();
            long fileSize = file.getSize();

            //  Store file (local)
            String fileUrl = fileStorageService.store(file);

            // Build entity
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setFileType(fileType);
            attachment.setFileSize(fileSize);
            attachment.setFileUrl(fileUrl);
            attachment.setUploadedAt(LocalDateTime.now());
            attachment.setStatus(AttachmentStatus.TEMP);

            // Save in DB
            Attachment saved = attachmentRepository.save(attachment);

            // Return DTO
            return attachmentMapper.toDto(saved);

        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public Page<AttachmentDtoResponse> getAllByTicketId(long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attachmentRepository.findAllByTicket_Id(id, pageable)
                .map(attachmentMapper::toDto);
    }

    @Override
    public AttachmentDtoResponse getById(Long id) {
        if(id <= 0){
            throw new ValidationException("Failed to find file");
        }
        Attachment attachment= attachmentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("file with this id not exist")
        );
        return attachmentMapper.toDto(attachment);
    }

    @Override
    public void deleteById(Long id) {

        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("file not found"));

        attachmentRepository.delete(attachment);
    }



    // auto remove unlinked Attachments to tickets every singe hour
    @Scheduled(cron = "0 0 * * * *")
    public void cleanOrPhanAttachments(){
        List<Attachment> orphanFiles = attachmentRepository.findByTicketIsNullAndStatus(AttachmentStatus.TEMP);
        for(Attachment att: orphanFiles){
            fileStorageService.delete(att.getFileUrl());
            attachmentRepository.delete(att);
        }
    }


}






















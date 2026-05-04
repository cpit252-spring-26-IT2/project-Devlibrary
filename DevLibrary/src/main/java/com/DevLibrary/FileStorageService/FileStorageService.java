package com.DevLibrary.FileStorageService;

import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.DevLibrary.ResourceFactory;
import com.DevLibrary.Entity.ResourceEntity;
import com.DevLibrary.exception.ResourceNotFoundException;
import com.DevLibrary.repository.ResourceRepository;
import com.DevLibrary.request.ResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.DevLibrary.DevLibrary.Resource;
import com.DevLibrary.DevLibrary.IdGenerator;

@Service
public class FileStorageService {

    private final ResourceRepository resourceRepository;
    private final IdGenerator idGenerator;


    public FileStorageService(ResourceRepository resourceRepository, IdGenerator idGenerator) {
        this.resourceRepository = resourceRepository;
        this.idGenerator = idGenerator;
    }
    public  ResourceEntity uploadFile(MultipartFile file, ResourceRequest request, String username) throws IOException{
        //  Save file to file system
        String fileName = file.getOriginalFilename();

        // Create the uploads folder if it does not already exist
        Path uploadPath = Paths.get("uploads").toAbsolutePath().normalize();

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Build the full path where the uploaded file will be saved
        Path filePath = uploadPath.resolve(fileName);

        file.transferTo(filePath.toFile());

        request.setFileName(fileName);
        request.setFileType(file.getContentType());
        request.setFilePath(filePath.toString());

        // Factory + Builder
        Resource resource = ResourceFactory.buildResource(request);

        //  Auto id and username
        resource.setId(idGenerator.generateId(request.getResourceType()));
        resource.setUploadedBy(username);

        // Convert Resource to ResourceEntity
        ResourceEntity entity = new ResourceEntity();
        entity.setId(resource.getId());
        entity.setTitle(resource.getTitle());
        entity.setCourseName(resource.getCourseName());
        entity.setDescription(resource.getDescription());
        entity.setUploadedBy(resource.getUploadedBy());
        entity.setResourceType(request.getResourceType());

        // If the resource has a file/link reference convert it to a string and save it in the entity reference field.
        if (resource.getReference() != null) {
            entity.setReference(resource.getReference().toString());
        }
        return resourceRepository.save(entity);

    }
    // Finds the resource by id gets its file path from reference and returns the file content as bytes for download.
    public byte[] downloadFile(String id) throws IOException {

        ResourceEntity resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        String filePath = resource.getReference();

        return Files.readAllBytes(Path.of(filePath));
    }
    // Finds the resource by id gets its file path from reference and extracts the original file name.
    public String getFileName(String id) {

        ResourceEntity resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        String filePath = resource.getReference();

        return Path.of(filePath).getFileName().toString();
    }
}

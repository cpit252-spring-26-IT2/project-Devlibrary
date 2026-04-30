package com.DevLibrary.resourceFacade;

import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.Entity.ResourceEntity;
import com.DevLibrary.FileStorageService.FileStorageService;
import com.DevLibrary.exception.ResourceNotFoundException;
import com.DevLibrary.repository.ResourceRepository;
import com.DevLibrary.request.ResourceRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
//This class provides a simple interface for resource operations.
@Service
public class ResourceFacade {
    private ResourceRepository repository;
    private IdGenerator idGenerator;
    private final FileStorageService fileStorageService;

    public ResourceFacade(ResourceRepository repository, IdGenerator idGenerator, FileStorageService fileStorageService) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.fileStorageService = fileStorageService;
    }
    //get all resources
    public List<ResourceEntity> getResources() {
        return repository.findAll();
    }

    //add
    public ResourceEntity  addResource(ResourceRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Factory + Builder
        Resource resource = ResourceFactory.buildResource(request);

        // auto id and username.
        resource.setId(idGenerator.generateId(request.getResourceType()));
        resource.setUploadedBy(username);
        //create a ResourceEntity that represents the table in mysql and fill it with attribute from resource object we made earlair by using Factory + Builder
        ResourceEntity entity = new ResourceEntity();
        entity.setId(resource.getId());
        entity.setTitle(resource.getTitle());
        entity.setCourseName(resource.getCourseName());
        entity.setDescription(resource.getDescription());
        entity.setUploadedBy(resource.getUploadedBy());
        entity.setResourceType(request.getResourceType());

        if (resource.getReference() != null) {
            entity.setReference(resource.getReference().toString());
        }

        fillSpecialFields(entity, resource);

        return repository.save(entity);
    }

    //see what the type of the resource if it was one of the below it will add it's values to entity
    //if was bookResource then it is book, take the auther and put it inside the entity
    private void fillSpecialFields(ResourceEntity entity, Resource resource) {
        if (resource instanceof BookResource book) {
            entity.setAuthor(book.getAuthor());
        }

        if (resource instanceof SlidesResource slides) {
            entity.setWeekNumber(slides.getWeekNumber());
        }

        if (resource instanceof NotesResource notes) {
            entity.setNoteType(notes.getNoteType());
        }

        if (resource instanceof ProjectResource project) {
            entity.setProjectLanguage(project.getProjectLanguage());
            entity.setProjectType(project.getProjectType());
        }
    }

    //delete
    public void deleteResource(String id) {
        //search by id using jpa commands findById() and then delete() if the resource doesn`t exist throw Exception
        ResourceEntity resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        repository.delete(resource);
    }
    //get
    public ResourceEntity getResource(String id) {
        //search by id using jpa commands findById() and then get it if the resource doesn`t exist throw Exception
        ResourceEntity resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        return resource;
    }
    //update
    public ResourceEntity updateResource(String id, ResourceRequest request) {
        // Search by id using JPA findById().
        // If the resource does not exist, throw ResourceNotFoundException.
        ResourceEntity updateresource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        // Update common fields
        if (request.getTitle() != null) {
            updateresource.setTitle(request.getTitle());
        }

        if (request.getCourseName() != null) {
            updateresource.setCourseName(request.getCourseName());
        }

        if (request.getDescription() != null) {
            updateresource.setDescription(request.getDescription());
        }

        if (request.getResourceType() != null) {
            updateresource.setResourceType(request.getResourceType());
        }

        // Update reference field
        if (request.getUrl() != null && !request.getUrl().isBlank()) {
            updateresource.setReference(request.getUrl());
        } else if (request.getFileName() != null && !request.getFileName().isBlank()
                && request.getFileType() != null && !request.getFileType().isBlank()
                && request.getFilePath() != null && !request.getFilePath().isBlank()) {

            updateresource.setReference(
                    request.getFileName() + " | " + request.getFileType() + " | " + request.getFilePath()
            );
        }

        // Update type-specific optional fields
        if (request.getAuthor() != null) {
            updateresource.setAuthor(request.getAuthor());
        }

        if (request.getWeekNumber() != null) {
            updateresource.setWeekNumber(request.getWeekNumber());
        }

        if (request.getNoteType() != null) {
            updateresource.setNoteType(request.getNoteType());
        }

        if (request.getProjectLanguage() != null) {
            updateresource.setProjectLanguage(request.getProjectLanguage());
        }

        if (request.getProjectType() != null) {
            updateresource.setProjectType(request.getProjectType());
        }

        return repository.save(updateresource);
    }
    // Uploads a file
    public ResourceEntity uploadFile(MultipartFile file, ResourceRequest request, String username) throws IOException {
        return fileStorageService.uploadFile(file, request, username);
    }
    // Downloads a file by its resource ID.
    public byte[] downloadFile(String id) throws IOException {
        return fileStorageService.downloadFile(id);
    }
    // Gets the original file name using the resource ID and its use to return the original name for the file you download.
    public String getFileName(String id) {
        return fileStorageService.getFileName(id);
    }
}
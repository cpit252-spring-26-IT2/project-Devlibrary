package com.DevLibrary.Controller;

import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.Entity.ResourceEntity;
import com.DevLibrary.exception.ResourceNotFoundException;
import com.DevLibrary.repository.ResourceRepository;
import com.DevLibrary.request.ResourceRequest;
import com.DevLibrary.resourceRefrence.FileReference;
import com.DevLibrary.resourceRefrence.LinkReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourceController {

    private final ResourceRepository resourceRepository;
    private final IdGenerator idGenerator;
    //temprary untill using database
    //private final List<Resource> resourcesList=new ArrayList<>();

    public ResourceController(IdGenerator idGenerator, ResourceRepository resourceRepository) {
        this.idGenerator = idGenerator;
        this.resourceRepository = resourceRepository;
    }

        @GetMapping("/resources")
        public List<ResourceEntity> getResourcesList () {
            return resourceRepository.findAll();
        }

    //For test GetMapping which will do reviewing Data
        @GetMapping("/test-add")
        public ResourceEntity testAddResource () {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            ResourceRequest request = new ResourceRequest();
            request.setResourceType("book");
            request.setTitle("Test Book");
            request.setCourseName("CPIT-252");
            request.setDescription("Test description");
            request.setAuthor("Test Author");

            request.setFileName("test-book.pdf");
            request.setFileType("pdf");
            request.setFilePath("/uploads/test-book.pdf");
            //use factory + builder
            Resource resource = ResourceFactory.buildResource(request);

            resource.setId(idGenerator.generateId(request.getResourceType()));
            resource.setUploadedBy(username);


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

            resourceRepository.save(entity);
            return entity;
        }

        //ADD
        @PostMapping("/add")
        public ResourceEntity addResource (@RequestBody ResourceRequest request){

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Factory + Builder
            Resource resource = ResourceFactory.buildResource(request);

            // auto id and username.
            resource.setId(idGenerator.generateId(request.getResourceType()));
            resource.setUploadedBy(username);

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

            resourceRepository.save(entity);
            return entity;
        }
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteResource (@PathVariable("id") String id){

            ResourceEntity resource = resourceRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

            resourceRepository.delete(resource);

            return ResponseEntity.ok().build();
        }
    }









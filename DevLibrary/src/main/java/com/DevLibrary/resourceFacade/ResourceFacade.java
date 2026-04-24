package com.DevLibrary.resourceFacade;

import com.DevLibrary.DevLibrary.IdGenerator;
import com.DevLibrary.DevLibrary.Resource;
import com.DevLibrary.DevLibrary.ResourceFactory;
import com.DevLibrary.Entity.ResourceEntity;
import com.DevLibrary.exception.ResourceNotFoundException;
import com.DevLibrary.repository.ResourceRepository;
import com.DevLibrary.request.ResourceRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ResourceFacade {
    private ResourceRepository repository;
    private IdGenerator idGenerator;

    public ResourceFacade(ResourceRepository repository,IdGenerator idGenerator) {
        this.repository = repository;
        this.idGenerator = idGenerator;
    }
    public List<ResourceEntity> getResources() {
        return repository.findAll();
    }
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
        return repository.save(entity);
    }
    public void deleteResource(String id) {
        //search by id using jpa commands findById() and then delete() if the resource doesn`t exist throw Exception
        ResourceEntity resource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        repository.delete(resource);
    }
}
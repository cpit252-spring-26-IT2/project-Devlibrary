package com.DevLibrary.Controller;

import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.Entity.ResourceEntity;
import com.DevLibrary.exception.ResourceNotFoundException;
import com.DevLibrary.repository.ResourceRepository;
import com.DevLibrary.request.ResourceRequest;
import com.DevLibrary.resourceFacade.ResourceFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourceController {
    private final ResourceFacade facade;


    public ResourceController(ResourceFacade facade) {
        this.facade = facade;
    }

        @GetMapping("/resources")
        public List<ResourceEntity> getResourcesList () {
            return facade.getResources();
        }
        @PostMapping("/add")
        public ResourceEntity addResource (@RequestBody ResourceRequest request){
            return facade.addResource(request);
        }
        @DeleteMapping("/delete/{id}")
        public ResponseEntity<Void> deleteResource (@PathVariable("id") String id){
        facade.deleteResource(id);
        return ResponseEntity.ok().build();
        }
        //@GetMapping("/resources/{id}") Later

        //@PutMapping("/update/{id}")   Later



    }









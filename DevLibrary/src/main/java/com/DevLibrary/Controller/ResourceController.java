package com.DevLibrary.Controller;

import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.request.ResourceRequest;
import com.DevLibrary.resourceRefrence.FileReference;
import com.DevLibrary.resourceRefrence.LinkReference;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourceController {

    private final IdGenerator idGenerator;
    //temprary untill using database
    private final List<Resource> resourcesList=new ArrayList<>();

    public ResourceController(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        loadSampleData();
    }
    //For test GetMapping which will do reviewing Data
    private void loadSampleData() {
    /*
     Temporary sample data for testing only.
     The uploadedBy value here is hardcoded because these resources are created
     automatically when the application starts, not by a logged-in user.

     Later, when using a database or real user actions, uploadedBy should come
     from the authenticated account through Spring Security.
    */
        if (!resourcesList.isEmpty()) {
            return;
        }

        BookResource book1 = new BookResource.Builder(
                "java basics '2' ",
                "CPIT-252",
                new FileReference("java-book2.pdf", "pdf", "/sample/java-book2.pdf")
        )
                .description("A helpful Java book for beginners 2")
                .author("John Smith")
                .build();

        book1.setId(idGenerator.generateId("book"));
        book1.setUploadedBy("sample-user");

        BookResource book2 = new BookResource.Builder(
                "java basics",
                "CPIT-251",
                new LinkReference("https://example.com/java-book")
        )
                .description("A helpful Java book for beginners")
                .author("John Smith")
                .build();

        book2.setId(idGenerator.generateId("book"));
        book2.setUploadedBy("sample-user");

        resourcesList.add(book1);
        resourcesList.add(book2);
    }

    @GetMapping("/resources")
    public List<Resource> getResourcesList(){
        return resourcesList;
    }


    @GetMapping("/test-add")
    public Resource testAddResource() {

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

        resourcesList.add(resource);
        return resource;
    }

    //ADD
    @PostMapping("/add")
    public Resource addResource(@RequestBody ResourceRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Factory + Builder
        Resource resource = ResourceFactory.buildResource(request);

        // auto id and username.
        resource.setId(idGenerator.generateId(request.getResourceType()));
        resource.setUploadedBy(username);

        resourcesList.add(resource);
        return resource;
    }


}

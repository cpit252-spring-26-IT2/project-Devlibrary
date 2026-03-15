package com.DevLibrary.Controller;

import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.request.ResourceRequest;
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
    private static List<Resource> resourcesList=new ArrayList<>();

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

        resourcesList.add(new BookResource(
                idGenerator.generateId("book"),
                "java basics '2' ",
                "CPIT-252",
                "A helpful Java book for beginners 2",
                "https://example.com/java-book2",
                "sample-user",
                "John Smith"
        ));

        resourcesList.add(new BookResource(
                idGenerator.generateId("book"),
                "java basics",
                "CPIT-251",
                "A helpful Java book for beginners",
                "https://example.com/java-book",
                "sample-user",
                "John Smith"
        ));
    }

    @GetMapping("/resources")
    public List<Resource> getResourcesList(){
        return resourcesList;
    }

    @GetMapping("/test-add")
    public Resource testAddResource() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Resource resource = new BookResource();
        resource.setId(idGenerator.generateId("book"));
        resource.setTitle("Test Book");
        resource.setCourseName("CPIT-252");
        resource.setDescription("Test description");
        resource.setLink("https://example.com/test");
        resource.setUploadedBy(username);
        ((BookResource) resource).setAuthor("Test Author");

        resourcesList.add(resource);
        return resource;
    }

    //ADD
    @PostMapping("/add")
    public Resource addResource(@RequestBody ResourceRequest request){
        Resource resource = ResourceFactory.generateResource(request.getResourceType());

        //this is for the sign in/log in system from security spring
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // auto-generated values
        resource.setId(idGenerator.generateId(request.getResourceType()));
        resource.setUploadedBy(username);
        //copy the values from postman and paste it in resource
        resource.setTitle(request.getTitle());
        resource.setCourseName(request.getCourseName());
        resource.setDescription(request.getDescription());
        resource.setLink(request.getLink());


        //here for the Attribute that are specific to a class so we make sure its belong to who the cast to the right one
        if (resource instanceof BookResource){
            ((BookResource)resource).setAuthor(request.getAuthor());
        }else if(resource instanceof SlidesResource){
            ((SlidesResource)resource).setWeekNumber(request.getWeekNumber());
        }else if(resource instanceof NotesResource){
            ((NotesResource)resource).setNoteType(request.getNoteType());
        }else if(resource instanceof ProjectResource){
            ((ProjectResource)resource).setProjectLanguage(request.getProjectLanguage());
            ((ProjectResource)resource).setProjectType(request.getProjectType());
        }

        resourcesList.add(resource);
        return resource;

    }


}

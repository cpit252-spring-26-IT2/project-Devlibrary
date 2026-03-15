package com.DevLibrary.Controller;


import com.DevLibrary.DevLibrary.*;
import com.DevLibrary.request.ResourceRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ResourceController {

    //temprary untill using database
    private static List<Resource> resourcesList=new ArrayList<>();
    //For test GetMapping which will do reviewing Data
    static{
        resourcesList.add(new BookResource("B1","java basics","CPIT-252","A helpful Java book for beginners","https://example.com/java-book","Azzam","John Smith"));
        resourcesList.add(new BookResource("B2","java basics2","CPIT-251","A helpful Java book for beginners","https://example.com/java-book","Azzam","John Smith"));
    }
    @GetMapping("/resources")
    public List<Resource> getResourcesList(){
        return resourcesList;
    }


    //ADD
    @PostMapping("/add")
    public Resource addResource(@RequestBody ResourceRequest request){
        Resource resource = ResourceFactory.generateResource(request.getResourceType());
        //copy the values from postman and paste it in resource
        resource.setId(request.getId());
        resource.setTitle(request.getTitle());
        resource.setCourseName(request.getCourseName());
        resource.setDescription(request.getDescription());
        resource.setLink(request.getLink());
        resource.setUploadedBy(request.getUploadedBy());

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

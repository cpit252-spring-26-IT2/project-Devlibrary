package com.DevLibrary.DevLibrary;

import com.DevLibrary.request.ResourceRequest;
import com.DevLibrary.resourceRefrence.ReferenceFactory;
import com.DevLibrary.resourceRefrence.ResourceReference;

/*
 Factory Pattern

 This class is responsible for creating the correct Resource object
 based on the resourceType provided by the user.

 Instead of letting the controller decide which class to create,
 the Controller **delegates** the creation process to this factory.

 The factory internally uses the Builder Pattern to construct the object
 while supporting optional attributes.
*/

public class ResourceFactory {

    public static Resource buildResource(ResourceRequest request) {

        ResourceReference reference = ReferenceFactory.createReference(
                request.getUrl(),
                request.getFileName(),
                request.getFileType(),
                request.getFilePath()
        );
        Resource resource;

        switch (request.getResourceType().toLowerCase()) {

            case "book":
                BookResource.Builder bookBuilder = new BookResource.Builder(
                        request.getTitle(),
                        request.getCourseName(),
                        reference
                );

                // if description is not empty get description
                if (request.getDescription() != null && !request.getDescription().isBlank()) {
                    bookBuilder.description(request.getDescription());
                }

                // if Author is not empty get Author
                if (request.getAuthor() != null && !request.getAuthor().isBlank()) {
                    bookBuilder.author(request.getAuthor());
                }

                // finish building
                resource = bookBuilder.build();
                break;

            case "slide":
            case "slides":
                SlidesResource.Builder slidesBuilder = new SlidesResource.Builder(
                        request.getTitle(),
                        request.getCourseName(),
                        reference
                );

                // if description is not empty get description
                if (request.getDescription() != null && !request.getDescription().isBlank()) {
                    slidesBuilder.description(request.getDescription());
                }

                // if WeekNumber is not empty get WeekNumber
                if (request.getWeekNumber() != null) {
                    slidesBuilder.weekNumber(request.getWeekNumber());
                }

                // finish building
                resource = slidesBuilder.build();
                break;

            case "note":
            case "notes":
                NotesResource.Builder notesBuilder = new NotesResource.Builder(
                        request.getTitle(),
                        request.getCourseName(),
                        reference
                );

                // if description is not empty get description
                if (request.getDescription() != null && !request.getDescription().isBlank()) {
                    notesBuilder.description(request.getDescription());
                }

                // if NoteType is not empty get NoteType
                if (request.getNoteType() != null && !request.getNoteType().isBlank()) {
                    notesBuilder.noteType(request.getNoteType());
                }

                // finish building
                resource = notesBuilder.build();
                break;

            case "project":
                ProjectResource.Builder projectBuilder = new ProjectResource.Builder(
                        request.getTitle(),
                        request.getCourseName(),
                        reference
                );

                // if description is not empty get description
                if (request.getDescription() != null && !request.getDescription().isBlank()) {
                    projectBuilder.description(request.getDescription());
                }

                // if ProjectLanguage is not empty get ProjectLanguage
                if (request.getProjectLanguage() != null && !request.getProjectLanguage().isBlank()) {
                    projectBuilder.projectLanguage(request.getProjectLanguage());
                }

                // if ProjectType is not empty get ProjectType
                if (request.getProjectType() != null && !request.getProjectType().isBlank()) {
                    projectBuilder.projectType(request.getProjectType());
                }

                // finish building
                resource = projectBuilder.build();
                break;

            default:
                throw new IllegalArgumentException("Unknown resource type");
        }

        return resource;
    }
}
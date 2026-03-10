package com.DevLibrary.DevLibrary;

public class ResorceFactory {


    public static Resource generateResource(String type){
        switch (type){
            case "slide":
                return new SlidesResource();
            case "book":
                return new BookResource();
            case "note":
                return new NotesResource();
            case "project":
                return new ProjectResource();

            default:
                throw new IllegalArgumentException("Unknown resource type");
        }

    }
}

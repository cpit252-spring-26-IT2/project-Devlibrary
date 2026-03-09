package com.DevLibrary.DevLibrary;

public class BookResource extends Resource {

    private String author;

    //here we can add a buiilder or another design pattren to enhans it but this is a first writing.
    public BookResource(String id, String title, String courseName, String description, String link, String uploadedBy, String author){
         super(id,title,courseName,description,link,uploadedBy);
        this.author =author;
}

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    //return the type Book because this is a Book type
    @Override
    public String getResourceType() {
        return "Book";
    }
}



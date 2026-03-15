package com.DevLibrary.DevLibrary;

public class BookResource extends Resource {

    private String author; // optional for books only


    public BookResource(){

    }

    private BookResource(Builder builder) {
        super(builder);
        this.author = builder.author;
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

    public static class Builder extends Resource.Builder<Builder> {
        private String author;

        public Builder(String title, String courseName, String link) {
            super(title, courseName, link);
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public BookResource build() {
            return new BookResource(this);
        }

        @Override
        protected Builder self() {
            return this;
            //allow for recursive generics.
            //to allow the resource to take this optionale special attributes from whatever class is having it.
        }
    }
}



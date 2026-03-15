package com.DevLibrary.DevLibrary;

public class NotesResource extends Resource {

    private String noteType; // optional

    public NotesResource(){
    }
    private NotesResource(Builder builder) {
        super(builder);
        this.noteType = builder.noteType;
    }
    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }


    //return the type notes because this is a note type
    @Override
    public String getResourceType() {
        return "Notes";
    }

    public static class Builder extends Resource.Builder<Builder> {
        private String noteType;

        public Builder(String title, String courseName, String link) {
            super(title, courseName, link);
        }

        public Builder noteType(String noteType) {
            this.noteType = noteType;
            return this;
        }

        public NotesResource build() {
            return new NotesResource(this);
        }

        @Override
        protected Builder self() {
            return this;
            //allow for recursive generics.
            //to allow the resource to take this optionale special attributes from whatever class is having it.
        }
    }
}

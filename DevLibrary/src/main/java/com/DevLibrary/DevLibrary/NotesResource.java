package com.DevLibrary.DevLibrary;


public class NotesResource extends Resource {

    private String noteType;


    //we can add design pattrens here
    public NotesResource(String id, String title, String courseName, String description, String link, String uploadedBy, String noteType) {
        super(id, title, courseName, description, link, uploadedBy);
        this.noteType = noteType;
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
}

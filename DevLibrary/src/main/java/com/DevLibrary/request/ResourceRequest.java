package com.DevLibrary.request;

// the use of this class is going to be getting data from user when use post request
public class ResourceRequest {
    private String resourceType;
    private String title;
    private String courseName;
    private String description;
    private String link;
    private String author;
    private Integer weekNumber;
    private String noteType;
    private String projectLanguage;
    private String projectType;


    public ResourceRequest() {
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getAuthor() {
        return author;
    }

    public String getNoteType() {
        return noteType;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public String getProjectLanguage() {
        return projectLanguage;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public void setProjectLanguage(String projectLanguage) {
        this.projectLanguage = projectLanguage;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}

package com.DevLibrary.DevLibrary;

public abstract class Resource{

    private String id;
    private String title;
    private String courseName;
    private String description;
    private String link;
    private String uploadedBy;

    public Resource(String id, String title, String courseName,String description,String link, String uploadedBy){
        this.id = id;
        this.title = title;
        this.courseName = courseName;
        this.description = description;
        this.link = link;
        this.uploadedBy = uploadedBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public abstract String getResourceType();

    public String toString() {
        return "Resource{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", link='" + link + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", resourceType='" + getResourceType() + '\'' +
                '}';
    }
}

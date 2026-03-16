package com.DevLibrary.request;

// the use of this class is going to be getting data from user when use post request
public class ResourceRequest {
    private String resourceType;
    private String title;
    private String courseName;
    private String description;

    private String url;
    private String fileName;
    private String fileType;
    private String filePath;

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

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFilePath() {
        return filePath;
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

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public void setProjectLanguage(String projectLanguage) {
        this.projectLanguage = projectLanguage;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}

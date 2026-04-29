package com.DevLibrary.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resources")
public class ResourceEntity {
    @Id
    private String id;

    private String title;
    private String courseName;
    private String description;
    private String uploadedBy;
    private String reference;
    private String resourceType;

    // Type-specific optional fields
    private String author;              // For BookResource
    private Integer weekNumber;         // For SlidesResource
    private String noteType;            // For NotesResource
    private String projectLanguage;     // For ProjectResource
    private String projectType;         // For ProjectResource

    public ResourceEntity() {
    }

    // getters and setters

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

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getProjectLanguage() {
        return projectLanguage;
    }

    public void setProjectLanguage(String projectLanguage) {
        this.projectLanguage = projectLanguage;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }
}
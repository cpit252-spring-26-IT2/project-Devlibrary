
package com.DevLibrary.DevLibrary;
import com.DevLibrary.resourceRefrence.ResourceReference;

public abstract class Resource{

    private String id;
    private String title;
    private String courseName;
    private String description;
    private ResourceReference reference;
    private String uploadedBy;

    /*
     Optional attributes for now (can be changed later if needed):
     - description
     - author (BookResource only)
     - weekNumber
     - noteType
     - projectLanguage
     - projectType
    */


    public Resource() {
    }

    protected Resource(Builder<?> builder) {
        this.title = builder.title;
        this.courseName = builder.courseName;
        this.reference = builder.reference;
        this.description = builder.description;
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

    public ResourceReference getReference() {
        return reference;
    }

    public void setReference(ResourceReference reference) {
        this.reference = reference;
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
                ", reference='" + reference + '\'' +
                ", uploadedBy='" + uploadedBy + '\'' +
                ", resourceType='" + getResourceType() + '\'' +
                '}';
    }
    public static abstract class Builder<T extends Builder<T>> {
        private final String title;
        private final String courseName;
        private final ResourceReference reference;
        private String description;

        public Builder(String title, String courseName, ResourceReference reference) {
            this.title = title;
            this.courseName = courseName;
            this.reference = reference;
        }

        public T description(String description) {
            this.description = description;
            return self();
        }

        protected abstract T self();
    }

}

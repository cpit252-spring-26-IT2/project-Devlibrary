package com.DevLibrary.DevLibrary;

import com.DevLibrary.resourceRefrence.ResourceReference;

public class ProjectResource extends Resource {

    private String projectLanguage;// optional
    private String projectType;// optional

    public ProjectResource() {
    }
    //builder
    private ProjectResource(Builder builder) {
        super(builder);
        this.projectLanguage = builder.projectLanguage;
        this.projectType = builder.projectType;
    }

    public String getProjectLanguage () {
        return projectLanguage;
    }

    public void setProjectLanguage (String projectLanguage){
        this.projectLanguage = projectLanguage;
    }

    public String getProjectType () {
        return projectType;
    }

    public void setProjectType (String projectType){
        this.projectType = projectType;
    }

//return the type Project because this is a Project type

    @Override
    public String getResourceType () {
        return "Project";
    }

    public static class Builder extends Resource.Builder<Builder> {
        private String projectLanguage;
        private String projectType;

        public Builder(String title, String courseName, ResourceReference reference) {
            super(title, courseName, reference);
        }

        public Builder projectLanguage(String projectLanguage) {
            this.projectLanguage = projectLanguage;
            return this;
        }

        public Builder projectType(String projectType) {
            this.projectType = projectType;
            return this;
        }

        public ProjectResource build() {
            return new ProjectResource(this);
        }

        @Override
        protected Builder self() {
            return this;
            //allow for recursive generics.
            //to allow the resource to take this optionale special attributes from whatever class is having it.
        }
    }
}
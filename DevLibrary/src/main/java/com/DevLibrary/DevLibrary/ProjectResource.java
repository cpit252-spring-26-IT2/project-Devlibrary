package com.DevLibrary.DevLibrary;

public class ProjectResource extends Resource {

    private String projectLanguage;
    private String projectType;

    public ProjectResource(String id, String title, String courseName, String description, String link, String uploadedBy, String projectLanguage) {

            super(id, title, courseName, description, link, uploadedBy);
            this.projectLanguage = projectLanguage;
            this.projectType = projectType;
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

}
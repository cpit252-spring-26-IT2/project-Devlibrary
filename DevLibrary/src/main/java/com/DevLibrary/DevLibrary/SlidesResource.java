package com.DevLibrary.DevLibrary;

public class SlidesResource extends Resource{

    private Integer weekNumber; // optional

    public SlidesResource() {
    }

    private SlidesResource(Builder builder) {
        super(builder);
        this.weekNumber = builder.weekNumber;
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    //return the type Slides because this is a Slides type
    @Override
    public String getResourceType() {
        return "Slides";
    }

    public static class Builder extends Resource.Builder<Builder> {
        private Integer weekNumber;

        public Builder(String title, String courseName, String link) {
            super(title, courseName, link);
        }

        public Builder weekNumber(Integer weekNumber) {
            this.weekNumber = weekNumber;
            return this;
        }

        public SlidesResource build() {
            return new SlidesResource(this);
        }

        @Override
        protected Builder self() {
            return this;
            //allow for recursive generics.
            //to allow the resource to take this optionale special attributes from whatever class is having it.
        }
    }
}
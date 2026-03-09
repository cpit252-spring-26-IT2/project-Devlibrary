package com.DevLibrary.DevLibrary;

public class SlidesResource extends Resource{

private int weekNumber;

//we can add design pattrens here
public SlidesResource(String id, String title, String courseName, String description, String link, String uploadedBy, int weekNumber) {
    super(id, title, courseName, description, link, uploadedBy);
    this.weekNumber = weekNumber;
}

public int getWeekNumber() {
    return weekNumber;
}

public void setWeekNumber(int weekNumber) {
    this.weekNumber = weekNumber;
}

//return the type Slides because this is a Slides type
@Override
public String getResourceType() {
    return "Slides";
}

}
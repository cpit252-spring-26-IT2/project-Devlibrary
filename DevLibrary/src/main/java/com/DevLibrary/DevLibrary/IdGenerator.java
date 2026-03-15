package com.DevLibrary.DevLibrary;

import org.springframework.stereotype.Component;

@Component //<-- this mean that the spring can inject it into the other classes like resourceController
public class IdGenerator {

    private int bookCounter = 1000;
    private int slideCounter = 1000;
    private int noteCounter = 1000;
    private int projectCounter = 1000;

    public String generateId(String type) {

        if(type.equalsIgnoreCase("book")){
            bookCounter++;
            return "B-" + bookCounter;
        }

        if(type.equalsIgnoreCase("slide") || type.equalsIgnoreCase("slides")){
            slideCounter++;
            return "S-" + slideCounter;
        }

        if(type.equalsIgnoreCase("note") || type.equalsIgnoreCase("notes")){
            noteCounter++;
            return "N-" + noteCounter;
        }

        if(type.equalsIgnoreCase("project")|| type.equalsIgnoreCase("projects")){
            projectCounter++;
            return "P-" + projectCounter;
        }

        return "R-0000";
    }
}
package com.DevLibrary.DevLibrary;

import com.DevLibrary.repository.ResourceRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component //<-- this mean that the spring can inject it into the other classes like resourceController
public class IdGenerator {

    private final ResourceRepository repository;

    public IdGenerator(ResourceRepository repository) {
        this.repository = repository;
    }

    public synchronized String generateId(String type) {
        String resourceType = type == null ? "" : type;
        String prefix = "R";

        if(resourceType.equalsIgnoreCase("book")){
            prefix = "B";
        } else if(resourceType.equalsIgnoreCase("slide") || resourceType.equalsIgnoreCase("slides")){
            prefix = "S";
        } else if(resourceType.equalsIgnoreCase("note") || resourceType.equalsIgnoreCase("notes")){
            prefix = "N";
        } else if(resourceType.equalsIgnoreCase("project")|| resourceType.equalsIgnoreCase("projects")){
            prefix = "P";
        }

        int nextNumber = getLatestNumber(prefix) + 1;
        return prefix + "-" + nextNumber;
    }

    private int getLatestNumber(String prefix) {
        List<String> ids = repository.findIdsByPrefix(prefix);
        return ids.stream()
                .map(id -> getNumberPart(prefix, id))
                .max(Integer::compareTo)
                .orElse(1000);
    }

    private int getNumberPart(String prefix, String id) {
        String expectedStart = prefix + "-";
        if (id == null || !id.startsWith(expectedStart)) {
            return 1000;
        }

        try {
            return Integer.parseInt(id.substring(expectedStart.length()));
        } catch (NumberFormatException ex) {
            return 1000;
        }
    }
}

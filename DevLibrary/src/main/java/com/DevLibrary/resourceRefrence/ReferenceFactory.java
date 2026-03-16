package com.DevLibrary.resourceRefrence;

public class ReferenceFactory {

    /*
     Factory Pattern:
     This factory decides which reference object should be created
     depending on the provided input, file or link.
    */
    public static ResourceReference createReference(String url, String fileName, String fileType, String filePath) {


        //if it has file name, file type and file path bring a file.
        if (fileName != null && !fileName.isBlank()
                && fileType != null && !fileType.isBlank()
                && filePath != null && !filePath.isBlank()) {
            return new FileReference(fileName, fileType, filePath);
        }


        //if there is a url it is a link bring the link.
        if (url != null && !url.isBlank()) {
            return new LinkReference(url);
        }

        throw new IllegalArgumentException("A valid file or link reference is required.");
    }
}
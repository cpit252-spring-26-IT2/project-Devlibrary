package com.DevLibrary.resourceRefrence;

public class FileReference extends ResourceReference {

    private String fileName;
    private String fileType;
    private String filePath;

    public FileReference() {
    }

    public FileReference(String fileName, String fileType, String filePath) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.filePath = filePath;
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

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getReferenceType() {
        return "FILE";
    }

    @Override
    public String toString() {
        return filePath;
    }
}
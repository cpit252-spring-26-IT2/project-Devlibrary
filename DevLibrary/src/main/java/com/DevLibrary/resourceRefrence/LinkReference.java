package com.DevLibrary.resourceRefrence;

public class LinkReference extends ResourceReference {

    private String url;

    public LinkReference() {
    }

    public LinkReference(String url) {
        this.url = url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String getReferenceType() {
        return "LINK";
    }

    @Override
    public String toString() {
        return url;
    }
}
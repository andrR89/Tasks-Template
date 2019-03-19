package com.devmasterteam.tasks.infra;

public class UrlBuilder {

    private String url;

    public UrlBuilder(String url) {
        this.url = url;
    }

    public void addResource(String value){
        this.url += "/" + value;
    }

    public String getUrl() {
        return url;
    }
}

package com.devmasterteam.tasks.entities;

import java.util.AbstractMap;

public class FullParameters {

    private String method;
    private String url;
    private AbstractMap<String, String> headerParameters;
    private AbstractMap<String, String> parameters;

    public FullParameters(String method, String url, AbstractMap<String, String> headerParameters, AbstractMap<String, String> parameters) {
        this.method = method;
        this.url = url;
        this.headerParameters = headerParameters;
        this.parameters = parameters;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AbstractMap<String, String> getHeaderParameters() {
        return headerParameters;
    }

    public void setHeaderParameters(AbstractMap<String, String> headerParameters) {
        this.headerParameters = headerParameters;
    }

    public AbstractMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(AbstractMap<String, String> parameters) {
        this.parameters = parameters;
    }
}

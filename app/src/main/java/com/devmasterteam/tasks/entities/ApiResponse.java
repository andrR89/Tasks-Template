package com.devmasterteam.tasks.entities;

public class ApiResponse {

    private String json;
    private int statusCode;

    public ApiResponse(String json, int statusCode) {
        this.json = json;
        this.statusCode = statusCode;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

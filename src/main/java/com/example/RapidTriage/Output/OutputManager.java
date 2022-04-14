package com.example.RapidTriage.Output;

public abstract class OutputManager {
    private String URL = "https://localhost:8080/api";

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}

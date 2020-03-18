package ru.job4j.models;

public class JsonResponse {

    private String status;

    public JsonResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

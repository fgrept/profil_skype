package com.example.projetfilrouge.pskype.exception;


public class ExceptionMessageResponse {

    private String date;
    private String message;
    private String path;

    public ExceptionMessageResponse(){

    }

    public ExceptionMessageResponse(String date, String message, String path) {

        this.date = date;
        this.message = message;
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ExceptionMessageResponse{" +
                "date='" + date + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}

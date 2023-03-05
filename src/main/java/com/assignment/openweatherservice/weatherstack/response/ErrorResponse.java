package com.assignment.openweatherservice.weatherstack.response;

public class ErrorResponse {

    private String success;

    private Error error;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}

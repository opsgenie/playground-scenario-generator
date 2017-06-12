package com.opsgenie.playground.scenarioGenerator.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Serhat Can
 * @version 09/06/17
 */
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer code;

    private String message;

    public ErrorResponse(String message, int code) {
        this.code = code;
        this.message = message;
    }

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

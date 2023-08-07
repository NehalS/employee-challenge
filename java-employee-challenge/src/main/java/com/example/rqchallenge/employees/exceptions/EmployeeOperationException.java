package com.example.rqchallenge.employees.exceptions;

import lombok.Getter;

@Getter
public class EmployeeOperationException extends RuntimeException {

    private Integer code;
    private String message;

    public EmployeeOperationException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public EmployeeOperationException(){}
}
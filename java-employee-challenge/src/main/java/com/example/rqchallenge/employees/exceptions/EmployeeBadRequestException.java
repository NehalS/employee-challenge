package com.example.rqchallenge.employees.exceptions;

public class EmployeeBadRequestException extends EmployeeOperationException {
    public EmployeeBadRequestException(Integer code, String message) {super(code, message);}
}
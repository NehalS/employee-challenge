package com.example.rqchallenge.employees.exceptions;

public class EmployeeNotFoundException extends EmployeeOperationException {
    public EmployeeNotFoundException(Integer code, String message) {super(code, message);}
}
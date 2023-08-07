package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.EmployeeResponse;
import com.example.rqchallenge.employees.exceptions.EmployeeBadRequestException;
import com.example.rqchallenge.employees.exceptions.EmployeeNotCreatedException;
import com.example.rqchallenge.employees.exceptions.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exceptions.EmployeeOperationException;
import com.example.rqchallenge.employees.utility.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmployeeExceptionHandler {

    @ExceptionHandler(EmployeeNotCreatedException.class)
    public ResponseEntity<?> handleEmployeeNotCreatedException(EmployeeNotCreatedException employeeNotCreatedException) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus(employeeNotCreatedException.getMessage());
        return new ResponseEntity<>(employeeResponse, HttpStatus.valueOf(employeeNotCreatedException.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleInternalException(Exception e) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus(Constants.INTERNAL_SERVER_ERROR_MESSAGE);
        return new ResponseEntity<>(employeeResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<?> handleEmployeeNotFoundException(EmployeeNotFoundException employeeNotFoundException) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus(employeeNotFoundException.getMessage());
        return new ResponseEntity<>(employeeResponse, HttpStatus.valueOf(employeeNotFoundException.getCode()));
    }

    @ExceptionHandler(EmployeeOperationException.class)
    public ResponseEntity<?> handleEmployeeOperationException (EmployeeOperationException employeeOperationException) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus(employeeOperationException.getMessage());
        return new ResponseEntity<>(employeeResponse, HttpStatus.valueOf(employeeOperationException.getCode()));
    }

    @ExceptionHandler(EmployeeBadRequestException.class)
    public ResponseEntity<?> handleEmployeeBadRequestException (EmployeeBadRequestException employeeBadRequestException) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus(employeeBadRequestException.getMessage());
        return new ResponseEntity<>(employeeResponse, HttpStatus.BAD_REQUEST);
    }
}
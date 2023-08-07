package com.example.rqchallenge.employees.validators;

import com.example.rqchallenge.employees.exceptions.EmployeeBadRequestException;
import com.example.rqchallenge.employees.utility.Constants;

import java.util.Map;

public class EmployeeValidator {

    public static boolean isValidEmployeeCreateRequest(Map<String, Object> employeeInput) {

        if(employeeInput == null || employeeInput.isEmpty()) {
            throw new EmployeeBadRequestException(Constants.BAD_REQUEST_STATUS_CODE, Constants.BAD_REQUEST_STATUS_MESSAGE);
        }
        long countNullKeyValues = employeeInput.entrySet().stream()
                .filter(entrySet -> entrySet.getValue() == null || entrySet.getKey() == null)
                .count();
        if(countNullKeyValues > 0) {
            throw new EmployeeBadRequestException(Constants.BAD_REQUEST_STATUS_CODE, Constants.BAD_REQUEST_STATUS_MESSAGE);
        }
        return true;
    }
}

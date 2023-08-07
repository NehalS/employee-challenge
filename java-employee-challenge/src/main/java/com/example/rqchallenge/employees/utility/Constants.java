package com.example.rqchallenge.employees.utility;

public class Constants {

    public static final String EMPLOYEE_CREATE_END_POINT = "/create";
    public static final String EMPLOYEE_DELETE_END_POINT = "/delete/%s";
    public static final String EMPLOYEE_GET_END_POINT = "/employee/%s";
    public static final String EMPLOYEE_GET_ALL_END_POINT = "/employees";

    public static final Integer ERROR_STATUS_CODE = 500;
    public static final String NOT_CREATED_STATUS_MESSAGE = "Unable to create employee";

    public static final Integer TOO_MANY_REQUESTS_STATUS_CODE = 429;
    public static final String TOO_MANY_REQUESTS_STATUS_MESSAGE = "Server is not serving requests now. Try again later !";

    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal error";

    public static final Integer EMPLOYEE_NOT_FOUND_STATUS_CODE = 404;
    public static final String EMPLOYEE_NOT_FOUND_STATUS_MESSAGE = "No employee found";

    public static final Integer BAD_REQUEST_STATUS_CODE = 400;
    public static final String BAD_REQUEST_STATUS_MESSAGE = "All parameters are mandatory";
}
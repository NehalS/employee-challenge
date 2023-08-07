package com.example.rqchallenge.employees.service;

import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.dto.EmployeeResponse;
import com.example.rqchallenge.employees.dto.EmployeesResponse;
import com.example.rqchallenge.employees.exceptions.EmployeeNotCreatedException;
import com.example.rqchallenge.employees.exceptions.EmployeeNotFoundException;
import com.example.rqchallenge.employees.exceptions.EmployeeOperationException;
import com.example.rqchallenge.employees.utility.Constants;
import com.example.rqchallenge.employees.utility.CustomRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeService implements IEmployeeService {

    @Autowired
    CustomRestTemplate customRestTemplate;

    @Value("${employee.service.baseURL}")
    private String employeeServiceBaseURL;

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Getting list of all employees");
        try {
            return getEmployees().getData();
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            throw new EmployeeNotCreatedException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        }
    }

    private EmployeesResponse getEmployees() {
        EmployeesResponse employeesResponse = customRestTemplate.execute(null,
                EmployeesResponse.class,
                Optional.empty(),
                HttpMethod.GET,
                null,
                new StringBuilder(employeeServiceBaseURL).append(Constants.EMPLOYEE_GET_ALL_END_POINT).toString()
        );
        log.info("Employees received successfully {}", employeesResponse);
        if (employeesResponse.getData() == null || employeesResponse.getData().isEmpty())
            throw new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_STATUS_CODE, Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE);
        return employeesResponse;
    }

    @Override
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        log.info("Searching employees with name {}", searchString);
        try {
            EmployeesResponse employeesResponse = getEmployees();
            List<Employee> employeeList = employeesResponse.getData().stream().parallel()
                    .filter(employee -> (searchString != null && searchString.equals(employee.getName())))
                    .collect(Collectors.toList());

            if (employeeList.isEmpty()) {
                log.info("No matching employee found with name {}", searchString);
                throw new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_STATUS_CODE, Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE);
            }

            return employeeList;
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.error("Error occurred while getting all employees", tooManyRequests);
            throw new EmployeeOperationException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        }
    }

    @Override
    public Employee getEmployeeById(String id) {
        log.info("Searching employee with id {}", id);
        try {
            EmployeeResponse employeeResponse = customRestTemplate.execute(null,
                    EmployeeResponse.class,
                    Optional.empty(),
                    HttpMethod.GET,
                    null,
                    new StringBuilder(employeeServiceBaseURL).append(String.format(Constants.EMPLOYEE_GET_END_POINT, id)).toString()
            );
            if (employeeResponse.getData() == null) {
                log.info("No employee found with id {}", id);
                throw new EmployeeNotFoundException(Constants.EMPLOYEE_NOT_FOUND_STATUS_CODE, Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE);
            }

            log.info("Employee received");
            return employeeResponse.getData();

        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.error("Error occurred while getting employee", tooManyRequests);
            throw new EmployeeOperationException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        log.info("Getting highest salary for employees");
        try {
            EmployeesResponse employeesResponse = getEmployees();
            log.info("Getting highest salary now");
            Integer highestSalary = employeesResponse.getData().stream()
                    .max(Comparator.comparing(Employee::getSalary))
                    //.collect(Collectors.maxBy(Comparator.comparing(Employee::getSalary)))
                    .get().getSalary().intValue();

            return highestSalary;
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.error("Error occurred while getting all employees", tooManyRequests);
            throw new EmployeeOperationException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        } catch (EmployeeNotFoundException e) {
            log.error("Error occurred while creating new employee", e);
            throw e;
        }
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        log.info("Getting Top Ten Highest Earning Employee Names");
        try {
            EmployeesResponse employeesResponse = getEmployees();
            log.info("Getting top ten salary employee names now");
            List<String> topTenHighestEarningEmpNames =  employeesResponse.getData().stream()
                    .sorted(Comparator.comparing(Employee::getSalary).reversed())
                    .limit(10)
                    .map(Employee::getName)
                    .collect(Collectors.toList());

            return topTenHighestEarningEmpNames;
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.error("Error occurred while getting all employees", tooManyRequests);
            throw new EmployeeOperationException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        } catch (EmployeeNotFoundException e) {
            log.error("Error occurred while getting TopTenHighestEarningEmployeeNames", e);
            throw e;
        }
    }

    @Override
    public String createEmployee(Map<String, Object> employeeInput) {
        log.info("Creating new employee {}", employeeInput);
        try {
            EmployeeResponse employeeResponse = customRestTemplate.execute(employeeInput,
                    EmployeeResponse.class,
                    Optional.of(MediaType.APPLICATION_JSON),
                    HttpMethod.POST,
                    null,
                    new StringBuilder(employeeServiceBaseURL).append(Constants.EMPLOYEE_CREATE_END_POINT).toString()
            );
            log.info("Employee created successfully {}", employeeResponse);
            return employeeResponse.getStatus();
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            throw new EmployeeNotCreatedException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        } catch (Exception e) {
            log.error("Error occurred while creating new employee", e);
            throw new EmployeeNotCreatedException(Constants.ERROR_STATUS_CODE, Constants.NOT_CREATED_STATUS_MESSAGE);
        }
    }

    @Override
    public String deleteEmployeeById(String id) {
        log.info("Deleting employee id {}", id);
        try {
            // First getting employee details as we have to return employee name which is deleted
            Employee employee =  getEmployeeById(id);
            EmployeeResponse employeeResponse = customRestTemplate.execute(null,
                    EmployeeResponse.class,
                    Optional.empty(),
                    HttpMethod.DELETE,
                    null,
                    new StringBuilder(employeeServiceBaseURL).append(String.format(Constants.EMPLOYEE_DELETE_END_POINT, id)).toString()
            );
            log.info("Employee deleted successfully {}", employeeResponse);
            return employee.getName();
        } catch (HttpClientErrorException.TooManyRequests tooManyRequests) {
            log.error("Error occurred while deleting employee", tooManyRequests);
            throw new EmployeeNotCreatedException(Constants.TOO_MANY_REQUESTS_STATUS_CODE, Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE);
        }
    }
}

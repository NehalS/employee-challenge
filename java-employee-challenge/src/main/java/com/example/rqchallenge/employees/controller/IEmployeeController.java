package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.Employee;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public interface IEmployeeController {

    @GetMapping(value = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Employee>> getAllEmployees();

    @GetMapping("/employees/name/{searchString}")
    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    @GetMapping("/employees/{id}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

    @GetMapping("/employees/highestSalary")
    ResponseEntity<Integer> getHighestSalaryOfEmployees();

    @GetMapping("/employees/topTenHighestEarningEmployeeNames")
    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    @PostMapping(value = "/employee", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> createEmployee(@RequestBody Map<String, Object> employeeInput);

    @DeleteMapping(value = "/employees/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

    /** Original Contacts
     *
     * @GetMapping()
     *     ResponseEntity<List < Employee>> getAllEmployees() throws IOException;
     *
     *     @GetMapping("/search/{searchString}")
     *     ResponseEntity<List < Employee>> getEmployeesByNameSearch(@PathVariable String searchString);
     *
     *     @GetMapping("/{id}")
     *     ResponseEntity<Employee> getEmployeeById(@PathVariable String id);
     *
     *     @GetMapping("/highestSalary")
     *     ResponseEntity<Integer> getHighestSalaryOfEmployees();
     *
     *     @GetMapping("/topTenHighestEarningEmployeeNames")
     *     ResponseEntity<List < String>> getTopTenHighestEarningEmployeeNames();
     *
     *     @PostMapping()
     *     ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput);
     *
     *     @DeleteMapping("/{id}")
     *     ResponseEntity<String> deleteEmployeeById(@PathVariable String id);
     */
}

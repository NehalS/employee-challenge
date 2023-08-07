package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.dto.Employee;
import com.example.rqchallenge.employees.dto.EmployeeResponse;
import com.example.rqchallenge.employees.dto.EmployeesResponse;
import com.example.rqchallenge.employees.utility.Constants;
import com.example.rqchallenge.employees.utility.CustomRestTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomRestTemplate customRestTemplate;

    @Test
    public void testCreateEmployeeSuccess() throws Exception {

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeeResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/employee").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "    \"name\" : \"emp3\",\n" +
                "    \"age\" : 38,\n" +
                "    \"salary\" : 20000\n" +
                "}");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isCreated());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("success"));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());

    }

    @Test
    public void testCreateEmployeeValidationFailed() throws Exception {

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/employee").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "    \"name\" : \"emp3\",\n" +
                "    \"age\" : 38,\n" +
                "    \"salary\" : null\n" +
                "}");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isBadRequest());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.BAD_REQUEST_STATUS_MESSAGE));

        verify(customRestTemplate, Mockito.times(0)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());

    }

    @Test
    public void testCreateEmployeeTooManyRequestException() throws Exception {

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenThrow(HttpClientErrorException.TooManyRequests.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/employee").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "    \"name\" : \"emp3\",\n" +
                "    \"age\" : 38,\n" +
                "    \"salary\" : 1000\n" +
                "}");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isTooManyRequests());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.TOO_MANY_REQUESTS_STATUS_MESSAGE));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testGetAllEmployeesSuccess() throws Exception {

        EmployeesResponse employeesResponse = new EmployeesResponse();
        Employee employee = new Employee();
        employee.setName("Tiger Nixon");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        employeesResponse.setData(Arrays.asList(employee));
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("Tiger Nixon"));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testGetAllEmployeesNoEmployeeFound() throws Exception {

        EmployeesResponse employeesResponse = new EmployeesResponse();
        employeesResponse.setData(Collections.emptyList());

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isNotFound());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testGetEmpByNameSuccess() throws Exception {

        EmployeesResponse employeesResponse = new EmployeesResponse();
        Employee employee = new Employee();
        employee.setName("Tiger Nixon");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        Employee employee2 = new Employee();
        employee2.setName("Michael Smith");
        employee2.setAge(52);
        employee2.setSalary(BigDecimal.valueOf(200000));
        employeesResponse.setData(Arrays.asList(employee, employee2));
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/name/Tiger Nixon");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("Tiger Nixon"));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testGetEmpByNameNotFound() throws Exception {

        EmployeesResponse employeesResponse = new EmployeesResponse();
        Employee employee = new Employee();
        employee.setName("Tiger Nixon");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        employeesResponse.setData(Arrays.asList(employee));
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/name/Ram");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isNotFound());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testGetEmpByIdSuccess() throws Exception {

        EmployeeResponse employeesResponse = new EmployeeResponse();
        Employee employee = new Employee();
        employee.setName("Tiger Nixon");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        employee.setId(1);
        employeesResponse.setData(employee);
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/1");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("1"));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testGetEmpByIdNotFound() throws Exception {

        EmployeeResponse employeesResponse = new EmployeeResponse();
        employeesResponse.setData(null);
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/1");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isNotFound());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testHighestSalaryOfEmployeesSuccess() throws Exception {

        EmployeesResponse employeesResponse = new EmployeesResponse();
        Employee employee = new Employee();
        employee.setName("Tiger Nixon");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        Employee employee2 = new Employee();
        employee2.setName("Smith");
        employee2.setAge(42);
        employee2.setSalary(BigDecimal.valueOf(330800));
        employeesResponse.setData(Arrays.asList(employee, employee2));
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/highestSalary");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("330800"));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testHighestSalaryOfEmployeesInternalError() throws Exception {

        EmployeesResponse employeesResponse = new EmployeesResponse();
        Employee employee = new Employee();
        employee.setName("Tiger Nixon");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        Employee employee2 = new Employee();
        employee2.setName("Smith");
        employee2.setAge(42);
        employee2.setSalary(null); // Setting salary null to internal error happen
        employeesResponse.setData(Arrays.asList(employee, employee2));
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/highestSalary");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isInternalServerError());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.INTERNAL_SERVER_ERROR_MESSAGE));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testHTopTenHighestEmpNamesSuccess() throws Exception {

        String[] empName = new String[]{"Rahul", "Sam", "Sita", "Joe", "Manthan", "John", "Ramesh", "Viaks", "Michael", "Maria", "Suresh"};
        List<Employee> empList = new ArrayList<>(11);
        Integer indexCount = 1;
        for(String name :  empName) {
            Employee employee = new Employee();
            employee.setName(name);
            employee.setSalary(BigDecimal.valueOf(1000 + indexCount));
            empList.add(employee);
            ++indexCount;
        }
        EmployeesResponse employeesResponse = new EmployeesResponse();
        employeesResponse.setData(empList);
        employeesResponse.setStatus("success");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeesResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/employees/topTenHighestEarningEmployeeNames");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("Sam"));
        assertTrue(response.contains("Suresh"));
        assertFalse(response.contains("Rahul"));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testDeleteEmpByIdSuccess() throws Exception {

        EmployeeResponse employeeResponse = new EmployeeResponse();
        Employee employee = new Employee();
        employee.setName("Tiger");
        employee.setAge(61);
        employee.setSalary(BigDecimal.valueOf(320800));
        employee.setId(1);
        employeeResponse.setData(employee);
        employeeResponse.setStatus("success");
        employeeResponse.setMessage("successfully! deleted Records");

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeeResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/employees/1");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isOk());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains("Tiger"));

        verify(customRestTemplate, Mockito.times(2)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }

    @Test
    public void testDeleteEmpByIdNotFound() throws Exception {

        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setData(null);

        when(customRestTemplate.execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any()))
                .thenReturn(employeeResponse);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/employees/1");

        ResultActions action = this.mockMvc.perform(builder);
        action.andExpect(MockMvcResultMatchers.status().isNotFound());
        String response = action.andReturn().getResponse().getContentAsString();
        assertTrue(response.contains(Constants.EMPLOYEE_NOT_FOUND_STATUS_MESSAGE));

        verify(customRestTemplate, Mockito.times(1)).execute(Mockito.any(), Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any(),Mockito.any());
    }
}
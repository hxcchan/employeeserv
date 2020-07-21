package com.paypal.bfs.test.employeeserv.impl;

import com.paypal.bfs.test.employeeserv.api.EmployeeResource;
import com.paypal.bfs.test.employeeserv.api.model.Employee;
import com.paypal.bfs.test.employeeserv.dao.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * Implementation class for employee resource.
 */
@RestController
public class EmployeeResourceImpl implements EmployeeResource {
    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    @Transactional
    public ResponseEntity<Employee> employeeGetById(String id) {

        Optional<Employee> result = employeeDAO.findById(Integer.parseInt(id));

        Employee employee = null;
        if (result.isPresent()){
            employee = result.get();
        } else {
            throw new RuntimeException("Did not find employee id - " + id);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @Override
    @Transactional
    public void createEmployee(@RequestBody Employee employee) {
        String firstName = employee.getFirstName();
        String lastName = employee.getLastName();
        Employee result = employeeDAO.findByFirstNameAndLastName(firstName, lastName);

        // Make sure no two same employees will be saved
        if(result == null){
            employeeDAO.save(employee);
        } else{
            throw new IllegalArgumentException("Employee already existed!");
        }
    }
}

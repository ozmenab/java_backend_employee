package com.abdullahozmen.business.services.impl;

import com.abdullahozmen.business.dto.EmployeeDto;
import com.abdullahozmen.business.services.EmployeeServices;
import com.abdullahozmen.data.entity.EmployeeEntity;
import com.abdullahozmen.data.repository.EmployeeRepository;
import com.abdullahozmen.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeServiceImpl implements EmployeeServices {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ModelMapper modelMapper;

    //http://localhost:8080/api/v1/employees
    @GetMapping("/employees")
    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<EmployeeDto> listDto = new ArrayList<>();
        Iterable<EmployeeEntity> entityList = employeeRepository.findAll();
        for (EmployeeEntity entity: entityList) {
            EmployeeDto employeeDto = entityToDto(entity);
            listDto.add(employeeDto);
        }
        return listDto;
    }

    //http://localhost:8080/api/v1/employees/add
    @PostMapping("/employees/add")
    @Override
    public EmployeeDto createEmployee(@RequestBody EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = dtoToEntity(employeeDto);
        employeeRepository.save(employeeEntity);
        return employeeDto;
    }

    //FIND
    //http://localhost:8080/api/v1/employees/1
    @GetMapping("/employees/{id}")
    @Override
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) throws Throwable {
        EmployeeEntity employee = (EmployeeEntity) employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee not exist with id "+id));
        EmployeeDto employeeDto = entityToDto(employee);
        return ResponseEntity.ok(employeeDto);
    }

    //UPDATE
    //http://localhost:8080/api/v1/employees
    @PutMapping("/employees/{id}")
    @Override
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDetails) throws Throwable {
        EmployeeEntity employeeEntity=dtoToEntity(employeeDetails);
        EmployeeEntity employee= (EmployeeEntity) employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not exist with id "+id));

        employee.setFirstName(employeeEntity.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmailId(employeeEntity.getEmailId());

        EmployeeEntity updatedEmployee=(EmployeeEntity) employeeRepository.save(employee);
        EmployeeDto employeeDto = entityToDto(employee);

        return ResponseEntity.ok(employeeDto);
    }


    //DELETE
    //http://localhost:8080/api/v1/employees/1
    @DeleteMapping("/employees/{id}")
    @Override
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) throws Throwable {
        EmployeeEntity employee = (EmployeeEntity) employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee not exist with id "+id));
        employeeRepository.delete(employee);
        Map<String,Boolean> response = new HashMap<>();
        return ResponseEntity.ok(response);
    }

///////////////////////////////////////////////////////////////////////////////////////////////

    //Model Mapper
    //Entity ==> DTO
    @Override
    public EmployeeDto entityToDto(EmployeeEntity employeeEntity) {
        EmployeeDto employeeDto=modelMapper.map(employeeEntity,EmployeeDto.class);
        return employeeDto;
    }

    //DTO ==> Entity
    @Override
    public EmployeeEntity dtoToEntity(EmployeeDto employeeDto) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDto,EmployeeEntity.class);
        return employeeEntity;
    }
}

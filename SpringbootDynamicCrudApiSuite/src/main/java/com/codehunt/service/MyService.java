package com.codehunt.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.codehunt.dto.EmployeeDTO;
import com.codehunt.entity.EmployeeEntity;





public interface MyService {

	EmployeeEntity insert( EmployeeDTO employeeDTO);

	List<EmployeeEntity> readAll();

	Optional<EmployeeEntity>  findBy(int id);

	EmployeeEntity updateAll( EmployeeDTO employeeDto,EmployeeEntity employeeData);

	List<String> validation(Map<String, Object> map);

	EmployeeEntity updatePatch(EmployeeEntity employeeEntity, Map<String, Object> map);

	Object delete(int id);
	
	

}

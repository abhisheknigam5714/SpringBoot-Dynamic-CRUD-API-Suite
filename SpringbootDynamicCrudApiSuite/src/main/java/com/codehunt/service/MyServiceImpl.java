package com.codehunt.service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codehunt.configuration.Myconfig;
import com.codehunt.controller.MyController;
import com.codehunt.dto.EmployeeDTO;
import com.codehunt.entity.EmployeeEntity;
import com.codehunt.repository.EmployeeRepo;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class MyServiceImpl implements MyService {

	@Autowired
	private EmployeeRepo employeerepo;

	@Autowired
	private ModelMapper modelmapper;

	private Validator validator;

	public MyServiceImpl() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		this.validator = validator;

	}

	@Override
	public EmployeeEntity insert(EmployeeDTO employeeDTO) {
		EmployeeEntity empEntity = modelmapper.map(employeeDTO, EmployeeEntity.class);
		empEntity.setDate(LocalDate.now());
		return employeerepo.save(empEntity);
	}

	@Override
	public List<EmployeeEntity> readAll() {
		return employeerepo.findAll();
	}

	@Override
	public Optional<EmployeeEntity> findBy(int id) {
		Optional<EmployeeEntity> byId = employeerepo.findById(id);
		return byId;
	}

	@Override
	public EmployeeEntity updateAll(EmployeeDTO employeeDto, EmployeeEntity employeeData) {
		modelmapper.map(employeeDto, employeeData);
		employeeData.setDate(LocalDate.now());
		return employeerepo.save(employeeData);

	}

	@Override
	public List<String> validation(Map<String, Object> map) {

		ArrayList<String> list = new ArrayList<>();
		for (Entry<String, Object> entry : map.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();

			try {
				Field field = EmployeeDTO.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				EmployeeDTO employeeDTO = new EmployeeDTO();
				field.set(employeeDTO, fieldValue);

				Set<ConstraintViolation<EmployeeDTO>> validate = validator.validate(employeeDTO);
				for (ConstraintViolation<EmployeeDTO> vialation : validate) {
					list.add(vialation.getMessage());
				}

			} catch (NoSuchFieldException e) {
				list.add("Field not exist" + " " + fieldName);

			} catch (IllegalArgumentException | IllegalAccessException e) {
				list.add("Something went wrong");

			} catch (Exception e) {
				list.add("Something went wrong");

			}

		}
		return list;

		/*
		 * ---------------this is the custom validation logic but not
		 * Scalable------------
		 * 
		 * HashSet<String> keys = new HashSet<>(Arrays.asList("name", "address",
		 * "salray")); ArrayList<String> list = new ArrayList<>(); for (Entry<String,
		 * Object> entry : map.entrySet()) { String key = entry.getKey(); Object value =
		 * entry.getValue();
		 * 
		 * if (!keys.contains(key)) { list.add("Key is wrong" + " " + key); }
		 * 
		 * if (key.equals("name")) { String name = (String) value; if (name.length() < 2
		 * || name.length() > 20) { list.add("Invalid name length"); } }
		 * 
		 * if (key.equals("address")) { String address = (String) value; if
		 * (address.length() < 2 || address.length() > 100) {
		 * list.add("Invalid address length"); }
		 * 
		 * }
		 * 
		 * if (key.equals("salary")) { Integer salary = (Integer) value; if (salary <
		 * 10000 || salary > 100000) { list.add("Invalid Salary"); }
		 * 
		 * } } return list;
		 */

	}

	@Override
	public EmployeeEntity updatePatch(EmployeeEntity employeeEntity, Map<String, Object> map) {
		for (Entry<String, Object> entry : map.entrySet()) {
			String fieldName = entry.getKey();
			Object fieldValue = entry.getValue();

			try {
				Field field = EmployeeEntity.class.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(employeeEntity, fieldValue);
			} catch (NoSuchFieldException e) {

				e.printStackTrace();
			} catch (SecurityException | IllegalAccessException e) {

				e.printStackTrace();
			}
			employeeEntity.setDate(LocalDate.now());
		}
		return employeerepo.save(employeeEntity);

		/*
		 * ---------- Again it is not scalable ---------- 
		 * if (fieldName.equals("name"))
		 * { employeeEntity.setName((String) fieldValue);
		 * 
		 * } if (fieldName.equals("address")) { employeeEntity.setAddress((String)
		 * fieldValue);
		 * 
		 * } if (fieldName.equals("salary")) { employeeEntity.setSalary((Integer)
		 * fieldValue);
		 * 
		 * } } employeeEntity.setDate(LocalDate.now());
		 * 
		 * return employeerepo.save(employeeEntity);
		 * 
		 */
	}

	@Override
	public Object delete(int id) {
		
				employeerepo.deleteById(id);
				return "Deleted";
		
	}
}

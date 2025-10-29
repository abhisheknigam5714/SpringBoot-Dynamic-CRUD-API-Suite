package com.codehunt.controller;

import java.net.http.HttpResponse.ResponseInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codehunt.dto.EmployeeDTO;
import com.codehunt.entity.EmployeeEntity;
import com.codehunt.service.MyService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/")
public class MyController {

	@Autowired
	private MyService myService;

	@PostMapping("employees")
	public ResponseEntity<?> insert(@Valid @RequestBody EmployeeDTO employeeDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			List<String> list = new ArrayList<>();
			List<ObjectError> allErrors = bindingResult.getAllErrors();

			for (ObjectError objectError : allErrors) {
				list.add(objectError.getDefaultMessage());

			}
			return new ResponseEntity<List<String>>(list, HttpStatus.BAD_REQUEST);
		} else {
			// service layer
			EmployeeEntity insert = myService.insert(employeeDTO);
			HashMap<Object, Object> hashMap = new HashMap<>();
			hashMap.put("message", "employee data saved successfully");
			hashMap.put("data", insert);

			return new ResponseEntity<>(hashMap, HttpStatus.CREATED);

		}

	}

	@GetMapping("employees")
	public ResponseEntity<List<EmployeeEntity>> readall() {
		List<EmployeeEntity> all = myService.readAll();
		return new ResponseEntity<>(all, HttpStatus.OK);

	}

	@GetMapping("employees/{id}")
	public ResponseEntity<?> readby(@PathVariable int id) {
		Optional<EmployeeEntity> data = myService.findBy(id);
		if (!data.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id _Not_Found");
		} else {
			return new ResponseEntity<>(data, HttpStatus.CREATED);
		}

	}

	@PutMapping("employees/{id}")
	public ResponseEntity<?> fullUpdate(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {

			List<String> list = new ArrayList<>();
			List<ObjectError> allErrors = bindingResult.getAllErrors();

			for (ObjectError objectError : allErrors) {
				list.add(objectError.getDefaultMessage());

			}
			return new ResponseEntity<List<String>>(list, HttpStatus.BAD_REQUEST);
		} else {
			Optional<EmployeeEntity> employeeData = myService.findBy(id);

			if (!employeeData.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id _Not_Found");
			} else {
				EmployeeEntity employeeEntity = employeeData.get();
				EmployeeEntity updateAll = myService.updateAll(employeeDto, employeeEntity);

				HashMap<Object, Object> hashMap = new HashMap<>();
				hashMap.put("message", "Employee Data Updated");
				hashMap.put("data", updateAll);
				return ResponseEntity.ok().body(hashMap);

			}

		}
	}

	@PatchMapping("employees/{id}")
	public ResponseEntity<?> patch(@PathVariable int id, @RequestBody Map<String, Object> map) {
		// 1.validation
		List<String> list = myService.validation(map);
		if (!list.isEmpty()) {
			return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);
		} else {
			// 2.id
			Optional<EmployeeEntity> data = myService.findBy(id);
			if (!data.isPresent()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id _Not_Found");
			}

			else {
				// 3.update
				EmployeeEntity employeeEntity = data.get();
				EmployeeEntity employee = myService.updatePatch(employeeEntity, map);
				HashMap<Object, Object> hashMap = new HashMap<>();
				hashMap.put("message", "Partial update done");
				hashMap.put("data", employee);
				return new ResponseEntity<>(hashMap, HttpStatus.OK);
			}

		}

	}
	
	@DeleteMapping("employees/{id}")
	public ResponseEntity<Object> delete(@PathVariable int id) {
		Optional<EmployeeEntity> byId = myService.findBy(id);
		if(byId.isPresent()) {
			Object delete = myService.delete(id);
			return new ResponseEntity<>(delete,HttpStatus.OK);
		}
		else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id does not exist");
		}
		
	}

}

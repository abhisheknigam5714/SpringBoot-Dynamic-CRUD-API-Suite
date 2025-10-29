package com.codehunt.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class EmployeeDTO {
	
	@Size(min = 3,max = 20 ,message = "NAME_SIZE_IS_NOT_VALID")
	private String name;
	
	@Size(min = 3,max = 50 ,message = "ADDRESS_SIZE_IS_NOT_VALID")
	private String address;
	
	@Min(2000)
	@Max(100000)
	private Integer salary;

}

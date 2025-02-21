package com.banking1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking1.entity.Employee;
import com.banking1.service.BankingServiceInterface;
import com.netflix.spectator.api.Utils;

@RestController
@RequestMapping("api/v1/emp")
public class BankingController {
	
	
	@Autowired
	private BankingServiceInterface bService;
	
	
	@GetMapping
	public List<Employee> displayAll() {
		try {
			return  bService.getAllRecordService();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<>();
		} 
	}
	
	
	@GetMapping("/{uid}")
	public Employee getEmployeeById(@PathVariable("uid") String email) {
		return bService.getEmployeeRecordByIdService(email); 
	}
	
	@GetMapping("/{uid}/{pass}")
	public Employee getEmployeeById(@PathVariable("uid") String email, @PathVariable("pass") String password) {
		return bService.findByEmailAndPassword(email, password);
	}


	@PostMapping
	public String registerEmployee(@RequestBody Employee emp) {
		
		return bService.createProfileService(emp);
	}
	
	@PutMapping("/{uid}")
	public String edit(@PathVariable("uid") String email,@RequestBody Employee emp) {
		return bService.editRecordService(emp);
		
	}
	
	@DeleteMapping("/{uid}")
	public String remove(@PathVariable("uid") String email) {
		return bService.deleteRecordService(email);
	} 
}
















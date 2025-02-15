package com.banking1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking1.dao.BankingDAOInterface;
import com.banking1.entity.Employee;


@Service
@Transactional
public class BankingService implements BankingServiceInterface{
	
	@Autowired
	private BankingDAOInterface bDao;

	@Override
	public String createProfileService(Employee emp) {
		bDao.save(emp);
		return "user registered successfully";
	}

	@Override
	public List<Employee> getAllRecordService() {
		return bDao.findAll();
	}

	@Override
	public String editRecordService(Employee emp) {
		bDao.saveAndFlush(emp);
		return "record edited";
	}

	@Override
	public String deleteRecordService(String email) {
		bDao.deleteById(email);
		return "record deleted";
	}

	@Override
	public Employee getEmployeeRecordByIdService(String email) {
	   Optional<Employee> ee=	  bDao.findById(email);
		return ee.get();
	}
	

	@Override
	public Employee findByEmailAndPassword(String email, String password) {
		return bDao.findByEmailAndPassword(email,password);
	}

}








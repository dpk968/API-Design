package com.banking1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banking1.dao.BankingDAOInterface;
import com.banking1.entity.Employee;

import io.github.resilience4j.timelimiter.annotation.TimeLimiter;


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
//	@TimeLimiter(name = "myTimeLimiter", fallbackMethod = "timeLimitFallback")
	public List<Employee> getAllRecordService() throws InterruptedException {
//		Thread.sleep(500);
		return bDao.findAll();
	}
//	public List<Employee> timeLimitFallback(Exception throwable) {
//	    System.out.println("Fallback triggered due to timeout: " + throwable.getMessage());
//	    return new ArrayList<>();
//	}

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








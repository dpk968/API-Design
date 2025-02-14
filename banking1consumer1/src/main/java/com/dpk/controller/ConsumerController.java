package com.dpk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dpk.dto.Employee;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/consumers")
public class ConsumerController {

	private RestTemplate template = new RestTemplate();

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping
	public List<Employee> consumeAllEmployee() {
		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();
		System.out.println(url);
		List<Employee> ee = template.getForObject(url + "/api/v1/emp", List.class);
//		System.out.println(template.getForEntity("http://localhost:8080/api/v1/emp", String.class));
		return ee;
	}

	@PostMapping
	public String createEmployee(@RequestBody Employee emp) {

		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();

		return template.postForObject(url + "/api/v1/emp", emp, String.class);
//		System.out.println(template.getForEntity("http://localhost:8080/api/v1/emp", String.class));

	}

	@PutMapping("{uid}")
	public String edit(@PathVariable("uid") String email, @RequestBody Employee emp) {
		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();
		
		template.put(url + "/api/v1/emp/" + email, emp, String.class);
		return "record updated";

	}

	@DeleteMapping("{uid}")
	public String remove(@PathVariable("uid") String email) {
		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();
		template.delete(url + "/api/v1/emp/" + email);
		return "record deleted";
	}

}

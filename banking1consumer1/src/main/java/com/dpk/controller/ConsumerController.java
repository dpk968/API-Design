package com.dpk.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.dpk.dto.Employee;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
@RequestMapping("/api/v1/consumers")
public class ConsumerController {

	private RestTemplate template = new RestTemplate();
	private static final String RETRY_NAME = "myRetry";

	@Autowired
	private DiscoveryClient discoveryClient;

	private static final String CIRCUIT_BREAKER_NAME = "myCircuitBreaker";

	@GetMapping
	@CircuitBreaker(name = CIRCUIT_BREAKER_NAME, fallbackMethod = "fallbackResponse")
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
	@Retry(name = RETRY_NAME, fallbackMethod = "fallbackPayment")
	public String createEmployee(@RequestBody Employee emp) {

		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();

		return template.postForObject(url + "/api/v1/emp", emp, String.class);
//		System.out.println(template.getForEntity("http://localhost:8080/api/v1/emp", String.class));

	}

	@PutMapping("{uid}")
	@RateLimiter(name = "myRateLimiter", fallbackMethod = "rateLimitFallback")
	public String edit(@PathVariable("uid") String email, @RequestBody Employee emp) {
		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();

		template.put(url + "/api/v1/emp/" + email, emp, String.class);
		return "record updated";

	}

	@DeleteMapping("{uid}")
	@Bulkhead(name = "myBulkhead", fallbackMethod = "bulkheadFallback")
	@TimeLimiter(name = "myTimeLimiter", fallbackMethod = "timeLimitFallback")
	public String remove(@PathVariable("uid") String email) throws InterruptedException {
		List<ServiceInstance> si = discoveryClient.getInstances("GATEWAY");

		ServiceInstance instance = si.get(0);

		String url = instance.getUri().toString();
		Thread.sleep(500);
		template.delete(url + "/api/v1/emp/" + email);
		return "record deleted";
	}

	public String rateLimitFallback(Exception e) {
		return "Too many requests! Please try again later.";
	}

	public CompletableFuture<String> timeLimitFallback(Exception e) {
		return CompletableFuture.supplyAsync(() -> "Request timed out! Try again later.");
	}

	public String bulkheadFallback(Exception e) {
		return "Too many users requesting! Try later.";
	}

	public String fallbackPayment(Exception e) {
		return "Payment failed after retries. Please try again later!";
	}

	public String fallbackResponse(Exception e) {
		return "Payment Service is down. Please try later!";
	}
}

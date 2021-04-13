package com.course.microservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "response-microservice")
public interface ResponseFeignClient {
	
	@GetMapping("api/responses/student/{id}/exam-responses")
	public Iterable<Long> getExamIdsWithResponseOfStudents(@PathVariable("id") Long studentId);

}

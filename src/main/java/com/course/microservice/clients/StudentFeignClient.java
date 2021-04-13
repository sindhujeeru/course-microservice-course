package com.course.microservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.microservice.users.entity.Student;

@FeignClient(name = "user-microservice")
public interface StudentFeignClient {
	

	@GetMapping("api/students/students-per-course")
	public Iterable<Student> getStudentsPerCourse(@RequestParam Iterable<Long> ids);

}

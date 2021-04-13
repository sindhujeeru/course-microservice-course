package com.course.microservice.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.common.microservice.service.CommonService;
import com.common.microservice.users.entity.Student;
import com.course.microservice.entity.Course;

public interface CourseService extends CommonService<Course>{
	
	public List<Course> findCourseByStudentId(Long id);
	
	public Iterable<Long> getExamIdsWithResponseOfStudents(@PathVariable("id") Long studentId);
	
	public Iterable<Student> getStudentsPerCourse(@RequestParam Iterable<Long> ids);
	
	public void removeCourseStudentById(Long id);

}

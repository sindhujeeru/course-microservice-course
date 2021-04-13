package com.course.microservice.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.microservice.service.CommonServiceImpl;
import com.common.microservice.users.entity.Student;
import com.course.microservice.clients.ResponseFeignClient;
import com.course.microservice.clients.StudentFeignClient;
import com.course.microservice.entity.Course;
import com.course.microservice.repository.CourseRepository;

@Service
public class CourseServiceImpl extends CommonServiceImpl<Course, CourseRepository> implements CourseService{
	
	@Autowired
	private ResponseFeignClient client;
	
	@Autowired
	private StudentFeignClient stClient;
	
	@Override
	@Transactional
	public List<Course> findCourseByStudentId(Long id) {
		
		return repository.findCourseByStudentId(id);
	}

	@Override
	@Transactional
	public Iterable<Long> getExamIdsWithResponseOfStudents(Long studentId) {
		return client.getExamIdsWithResponseOfStudents(studentId);
	}

	@Override
	@Transactional
	public Iterable<Student> getStudentsPerCourse(Iterable<Long> ids) {
		return stClient.getStudentsPerCourse(ids);
	}

	@Override
	@Transactional
	public void removeCourseStudentById(Long id) {
		repository.removeCourseStudentById(id);
		
	}
	
}

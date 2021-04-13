package com.course.microservice.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.common.exam.microservice.entity.Exam;
import com.common.microservice.controller.CommonController;
import com.common.microservice.users.entity.Student;
import com.course.microservice.entity.Course;
import com.course.microservice.entity.CourseStudent;
import com.course.microservice.service.CourseService;


@RestController
@RequestMapping("/api/courses")
public class CourseController extends CommonController<Course, CourseService>{
	
	
	
	@Override
	@GetMapping
	public ResponseEntity<?> listUsers() {
		
		List<Course> courses = ((List<Course>) service.findAll())
				.stream().map(c ->{
					c.getCourseStudents().forEach(cs ->{
						Student student = new Student();
						student.setId(cs.getStudentId());
						
						c.addStudent(student);
					});
					return c;
				}).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(courses);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody Course course, BindingResult result){
		
		if(result.hasErrors()) {
			this.validate(result);
		}
		
		Optional<Course> CourseOptional = service.findById(id);
		
		if(CourseOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Course courseDb = CourseOptional.get();
		
		courseDb.setName(course.getName());
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
	}
	
	@PutMapping("/{id}/add-student")
	public ResponseEntity<?> addStudents(@Valid @RequestBody List<Student> students,BindingResult result, @PathVariable("id") Long id){
		
		if(result.hasErrors()) {
			return this.validate(result);
		}
		
		Optional<Course> CourseOptional = service.findById(id);
		
		if(CourseOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Course courseDb = CourseOptional.get();
		
		students.forEach(a -> {
			CourseStudent courseStudent = new CourseStudent();
			courseStudent.setStudentId(a.getId());
			courseStudent.setCourse(courseDb);
			courseDb.addCourseStudent(courseStudent);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
	}
	
	@PutMapping("/{id}/add-exam")
	public ResponseEntity<?> updateExam(@PathVariable("id") Long id, @RequestBody List<Exam> exams){
		
		Optional<Course> CourseOptional = service.findById(id);
		
		if (CourseOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Course courseDb = CourseOptional.get();
		
		exams.forEach(a -> {
			courseDb.addExam(a);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
		
	}
	
	@PutMapping("/{id}/remove-student")
	public ResponseEntity<?> removeStudent(@RequestBody Student student, @PathVariable("id") Long id){
		Optional<Course> CourseOptional = service.findById(id);
		
		if(CourseOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Course courseDb = CourseOptional.get();
		CourseStudent courseStudent = new CourseStudent();
		courseStudent.setStudentId(student.getId());
		courseDb.removeCourseStudent(courseStudent);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
	}
	
	@PutMapping("/{id}/remove-exam")
	public ResponseEntity<?> removeStudent(@RequestBody Exam exam, @PathVariable("id") Long id){
		Optional<Course> CourseOptional = service.findById(id);
		
		if(CourseOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Course courseDb = CourseOptional.get();
		
		courseDb.removeExam(exam);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(courseDb));
	}	
	
	@GetMapping("/student/{id}")
	public ResponseEntity<?> searchforStudentId(@PathVariable("id") Long id){
		List<Course> course= service.findCourseByStudentId(id);
		
		if(course != null) {
			List<Long> examIds = (List<Long>) service.getExamIdsWithResponseOfStudents(id);
			
			if(examIds != null && examIds.size() >0) 
			{
			
				List<Exam> exams = course.get(0).getExams().stream().map(ex ->{
					if(examIds.contains(ex.getId())) {
						ex.setResponseVal(true);
					}
					return ex;
				}).collect(Collectors.toList());
				
				course.get(0).setExams(exams);
			}
		}
		
		return ResponseEntity.ok(course);
		
	}
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<?> getUserbyId(@PathVariable("id") Long id){
		
		Optional<Course> courseOptional = service.findById(id);
		
		if(courseOptional.isEmpty()) {
			ResponseEntity.notFound().build();
		}
		
		Course course = courseOptional.get();
		
		if(course.getCourseStudents().isEmpty()==false) {
			List<Long> ids = course.getCourseStudents()
							.stream().map(cs -> {
								return cs.getStudentId();
							}).collect(Collectors.toList());
			List<Student> students = (List<Student>) service.getStudentsPerCourse(ids);
			
			course.setStudents(students);
		}
		return ResponseEntity.ok().body(course);
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> list(Pageable pageable){
		Page<Course> courses = service.findAll(pageable)
				.map(course ->{
					course.getCourseStudents().forEach(cs ->{
						Student student = new Student();
						student.setId(cs.getStudentId());
						course.addStudent(student);
					});
					
					return course;
				});
		
		return ResponseEntity.ok().body(courses);
	}
	
	@DeleteMapping("/remove-student/{id}")
	public ResponseEntity<?> removeCourseStudentById(@PathVariable("id") Long id){
		
		service.removeCourseStudentById(id);
		return ResponseEntity.noContent().build();
	}

}










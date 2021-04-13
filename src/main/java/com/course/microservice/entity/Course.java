package com.course.microservice.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.common.exam.microservice.entity.Exam;
import com.common.microservice.users.entity.Student;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "courses")
@Getter
@Setter
@AllArgsConstructor
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String name;
	
	@Column(name = "create_at")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt; 
	
	//@ManyToMany(fetch = FetchType.LAZY)
	@Transient
	private List<Student> students;
	
	@JsonIgnoreProperties(value = {"course"},allowSetters = true)
	@OneToMany(fetch = FetchType.LAZY,
			mappedBy ="course", cascade = CascadeType.ALL,
			orphanRemoval = true)
	private List<CourseStudent> courseStudents;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Exam> exams;
	
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}
	
	public Course() {
		this.students = new ArrayList<Student>();
		this.exams = new ArrayList<Exam>();
		this.courseStudents = new ArrayList<CourseStudent>();
	}
	
	public void addStudent(Student student) {
		this.students.add(student);
	}
	
	public void removeStudent(Student student) {
		this.students.remove(student);
		
	}
	
	public void addExam(Exam exam) {
		this.exams.add(exam);
	}
	
	public void removeExam(Exam exam) {
		this.exams.remove(exam);
	}
	
	public void addCourseStudent(CourseStudent courseStudent) {
		this.courseStudents.add(courseStudent);
	}
	
	public void removeCourseStudent(CourseStudent courseStudent) {
		this.courseStudents.remove(courseStudent);
	}

}

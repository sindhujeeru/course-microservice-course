package com.course.microservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.course.microservice.entity.Course;

public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {
	
	@Query("select c from Course c join fetch c.courseStudents a where a.studentId=?1")
	public List<Course> findCourseByStudentId(Long id);
	
	@Modifying
	@Query("delete from CourseStudent ca where ca.studentId=?1")
	public void removeCourseStudentById(Long id);
}

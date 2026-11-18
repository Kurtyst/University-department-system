
package com.champlain.university.boostrap;

import com.champlain.university.domain.domainlayer.*;
import com.champlain.university.repository.dataaccesslayer.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seed(
            StudentRepository students,
            ProfessorRepository professors,
            CourseRepository courses,
            EnrollmentRepository enrollments
    ) {
        return args -> {

            if (students.count() > 0) {
                return;
            }

            List<Student> studentList = new ArrayList<>();
            List<Professor> professorList = new ArrayList<>();
            List<Course> courseList = new ArrayList<>();

            for (int i = 1; i <= 10; i++) {

                Student student = new Student();
                student.setFirstName("Student" + i);
                student.setLastName("Example");
                student.setEmail("student" + i + "@university.example");

                studentList.add(students.save(student));

                Professor professor = new Professor();
                professor.setFirstName("Professor" + i);
                professor.setLastName("Example");
                professor.setEmail("professor" + i + "@university.example");

                professorList.add(professors.save(professor));
            }

            for (int i = 1; i <= 10; i++) {

                Course course = new Course();
                course.setCode("COMP" + (100 + i));
                course.setTitle("Computer Science Course " + i);
                course.setCredits(3);
                course.setProfessor(professorList.get(i - 1));

                courseList.add(courses.save(course));
            }

            for (int i = 0; i < 20; i++) {

                Enrollment enrollment = new Enrollment();

                enrollment.setStudent(studentList.get(i % 10));
                enrollment.setCourse(
                        courseList.get((i + (i / 10)) % 10)
                );
                enrollment.setEnrolledAt(
                        LocalDate.now().minusDays(i)
                );

                enrollments.save(enrollment);
            }
        };
    }
}

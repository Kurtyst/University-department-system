
package com.champlain.university.service.businesslogiclayer;

import com.champlain.university.domain.domainlayer.Course;
import com.champlain.university.domain.domainlayer.Enrollment;
import com.champlain.university.domain.domainlayer.Student;
import com.champlain.university.dto.request.EnrollmentRequestDTO;
import com.champlain.university.dto.response.EnrollmentResponseDTO;
import com.champlain.university.exception.ConflictException;
import com.champlain.university.exception.NotFoundException;
import com.champlain.university.mapper.EnrollmentMapper;
import com.champlain.university.repository.dataaccesslayer.CourseRepository;
import com.champlain.university.repository.dataaccesslayer.EnrollmentRepository;
import com.champlain.university.repository.dataaccesslayer.StudentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponseDTO> all() {
        return enrollmentRepository.findAll()
                .stream()
                .map(EnrollmentMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnrollmentResponseDTO one(Long id) {
        return EnrollmentMapper.toDTO(findEnrollment(id));
    }

    public EnrollmentResponseDTO create(EnrollmentRequestDTO dto) {
        Student student = findStudent(dto.studentId());
        Course course = findCourse(dto.courseId());

        if (enrollmentRepository.existsByStudentIdAndCourseId(
                dto.studentId(),
                dto.courseId()
        )) {
            throw new ConflictException(
                    "Student is already enrolled in this course"
            );
        }

        Enrollment enrollment = EnrollmentMapper.toEntity(
                dto,
                student,
                course
        );

        return EnrollmentMapper.toDTO(
                enrollmentRepository.save(enrollment)
        );
    }

    public EnrollmentResponseDTO update(
            Long id,
            EnrollmentRequestDTO dto
    ) {
        Enrollment enrollment = findEnrollment(id);

        Student student = findStudent(dto.studentId());
        Course course = findCourse(dto.courseId());

        boolean sameEnrollment =
                enrollment.getStudent().getId().equals(dto.studentId())
                        && enrollment.getCourse().getId().equals(dto.courseId());

        if (!sameEnrollment &&
                enrollmentRepository.existsByStudentIdAndCourseId(
                        dto.studentId(),
                        dto.courseId()
                )) {
            throw new ConflictException(
                    "Student is already enrolled in this course"
            );
        }

        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(dto.enrolledAt());
        enrollment.setGrade(dto.grade());

        return EnrollmentMapper.toDTO(
                enrollmentRepository.save(enrollment)
        );
    }

    public void delete(Long id) {
        Enrollment enrollment = findEnrollment(id);
        enrollmentRepository.delete(enrollment);
    }

    private Enrollment findEnrollment(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Enrollment not found with ID: " + id
                        )
                );
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Student not found with ID: " + id
                        )
                );
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Course not found with ID: " + id
                        )
                );
    }
}

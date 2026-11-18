
package com.champlain.university.mapper;

import com.champlain.university.domain.domainlayer.Enrollment;
import com.champlain.university.domain.domainlayer.Student;
import com.champlain.university.domain.domainlayer.Course;
import com.champlain.university.dto.request.EnrollmentRequestDTO;
import com.champlain.university.dto.response.EnrollmentResponseDTO;

public class EnrollmentMapper {

    public static EnrollmentResponseDTO toDTO(
            Enrollment enrollment
    ) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getStudent().getId(),
                enrollment.getCourse().getId(),
                enrollment.getEnrolledAt(),
                enrollment.getGrade()
        );
    }

    public static Enrollment toEntity(
            EnrollmentRequestDTO dto,
            Student student,
            Course course
    ) {
        return new Enrollment(
                student,
                course,
                dto.enrolledAt(),
                dto.grade()
        );
    }
}

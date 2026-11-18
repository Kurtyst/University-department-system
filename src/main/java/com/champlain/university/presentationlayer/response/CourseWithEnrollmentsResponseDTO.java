
package com.champlain.university.dto.response;

import java.util.List;

public record CourseWithEnrollmentsResponseDTO(

        CourseResponseDTO course,

        List<EnrollmentResponseDTO> enrollments

) {
}

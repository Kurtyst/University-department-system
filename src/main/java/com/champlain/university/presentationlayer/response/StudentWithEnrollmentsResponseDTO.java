
package com.champlain.university.dto.response;

import java.util.List;

public record StudentWithEnrollmentsResponseDTO(

        StudentResponseDTO student,

        List<EnrollmentResponseDTO> enrollments

) {
}


package com.champlain.university.dto.response;

import java.time.LocalDate;

public record EnrollmentResponseDTO(

        Long id,

        Long studentId,

        Long courseId,

        LocalDate enrolledAt,

        Integer grade

) {
}

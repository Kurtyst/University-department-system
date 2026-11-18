
package com.champlain.university.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record EnrollmentRequestDTO(

        @NotNull(message = "Student ID is required")
        Long studentId,

        @NotNull(message = "Course ID is required")
        Long courseId,

        @NotNull(message = "Enrollment date is required")
        LocalDate enrolledAt,

        @Min(value = 0, message = "Grade must be at least 0")
        @Max(value = 100, message = "Grade cannot exceed 100")
        Integer grade

) {
}

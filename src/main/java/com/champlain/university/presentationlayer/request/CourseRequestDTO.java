
package com.champlain.university.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequestDTO(

        @NotBlank(message = "Course code is required")
        String code,

        @NotBlank(message = "Course title is required")
        String title,

        @NotNull(message = "Credits are required")
        @Min(value = 1, message = "Credits must be at least 1")
        Integer credits,

        @NotNull(message = "Professor ID is required")
        Long professorId

) {
}

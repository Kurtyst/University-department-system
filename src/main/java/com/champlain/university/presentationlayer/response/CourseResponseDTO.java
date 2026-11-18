
package com.champlain.university.dto.response;

public record CourseResponseDTO(

        Long id,

        String code,

        String title,

        Integer credits,

        Long professorId

) {
}

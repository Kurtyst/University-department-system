
package com.champlain.university.mapper;

import com.champlain.university.domain.domainlayer.Course;
import com.champlain.university.domain.domainlayer.Professor;
import com.champlain.university.dto.request.CourseRequestDTO;
import com.champlain.university.dto.response.CourseResponseDTO;

public class CourseMapper {

    public static CourseResponseDTO toDTO(Course course) {
        return new CourseResponseDTO(
                course.getId(),
                course.getCode(),
                course.getTitle(),
                course.getCredits(),
                course.getProfessor().getId()
        );
    }

    public static Course toEntity(
            CourseRequestDTO dto,
            Professor professor
    ) {
        return new Course(
                dto.code(),
                dto.title(),
                dto.credits(),
                professor
        );
    }
}


package com.champlain.university.mapper;

import com.champlain.university.domain.domainlayer.Student;
import com.champlain.university.dto.request.StudentRequestDTO;
import com.champlain.university.dto.response.StudentResponseDTO;

public class StudentMapper {

    public static StudentResponseDTO toDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }

    public static Student toEntity(StudentRequestDTO dto) {
        return new Student(
                dto.firstName(),
                dto.lastName(),
                dto.email()
        );
    }
}

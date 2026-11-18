
package com.champlain.university.mapper;

import com.champlain.university.domain.domainlayer.Professor;
import com.champlain.university.dto.request.ProfessorRequestDTO;
import com.champlain.university.dto.response.ProfessorResponseDTO;

public class ProfessorMapper {

    public static ProfessorResponseDTO toDTO(Professor professor) {
        return new ProfessorResponseDTO(
                professor.getId(),
                professor.getFirstName(),
                professor.getLastName(),
                professor.getEmail()
        );
    }

    public static Professor toEntity(ProfessorRequestDTO dto) {
        return new Professor(
                dto.firstName(),
                dto.lastName(),
                dto.email()
        );
    }
}

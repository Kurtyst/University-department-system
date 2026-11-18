
package com.champlain.university.service.businesslogiclayer;

import com.champlain.university.domain.domainlayer.Professor;
import com.champlain.university.dto.request.ProfessorRequestDTO;
import com.champlain.university.dto.response.ProfessorResponseDTO;
import com.champlain.university.exception.ConflictException;
import com.champlain.university.exception.NotFoundException;
import com.champlain.university.mapper.ProfessorMapper;
import com.champlain.university.repository.dataaccesslayer.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponseDTO> all() {
        return professorRepository.findAll()
                .stream()
                .map(ProfessorMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProfessorResponseDTO one(Long id) {
        return ProfessorMapper.toDTO(findProfessor(id));
    }

    public ProfessorResponseDTO create(ProfessorRequestDTO dto) {
        if (professorRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Professor email already exists");
        }

        Professor professor = ProfessorMapper.toEntity(dto);

        return ProfessorMapper.toDTO(
                professorRepository.save(professor)
        );
    }

    public ProfessorResponseDTO update(
            Long id,
            ProfessorRequestDTO dto
    ) {
        Professor professor = findProfessor(id);

        professorRepository.findByEmail(dto.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "Professor email already exists"
                    );
                });

        professor.setFirstName(dto.firstName());
        professor.setLastName(dto.lastName());
        professor.setEmail(dto.email());

        return ProfessorMapper.toDTO(
                professorRepository.save(professor)
        );
    }

    public void delete(Long id) {
        Professor professor = findProfessor(id);
        professorRepository.delete(professor);
    }

    private Professor findProfessor(Long id) {
        return professorRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Professor not found with ID: " + id
                        )
                );
    }
}

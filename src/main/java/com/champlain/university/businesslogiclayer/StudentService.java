
package com.champlain.university.service.businesslogiclayer;

import com.champlain.university.domain.domainlayer.Student;
import com.champlain.university.dto.request.StudentRequestDTO;
import com.champlain.university.dto.response.StudentResponseDTO;
import com.champlain.university.exception.ConflictException;
import com.champlain.university.exception.NotFoundException;
import com.champlain.university.mapper.StudentMapper;
import com.champlain.university.repository.dataaccesslayer.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentResponseDTO> all() {
        return studentRepository.findAll()
                .stream()
                .map(StudentMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResponseDTO one(Long id) {
        return StudentMapper.toDTO(findStudent(id));
    }

    public StudentResponseDTO create(StudentRequestDTO dto) {
        if (studentRepository.existsByEmail(dto.email())) {
            throw new ConflictException("Student email already exists");
        }

        Student student = StudentMapper.toEntity(dto);
        return StudentMapper.toDTO(studentRepository.save(student));
    }

    public StudentResponseDTO update(Long id, StudentRequestDTO dto) {
        Student student = findStudent(id);

        studentRepository.findByEmail(dto.email())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Student email already exists");
                });

        student.setFirstName(dto.firstName());
        student.setLastName(dto.lastName());
        student.setEmail(dto.email());

        return StudentMapper.toDTO(studentRepository.save(student));
    }

    public void delete(Long id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    private Student findStudent(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Student not found with ID: " + id
                        )
                );
    }
}

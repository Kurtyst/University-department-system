
package com.champlain.university.controller.presentationlayer;

import com.champlain.university.dto.request.StudentRequestDTO;
import com.champlain.university.dto.response.StudentResponseDTO;
import com.champlain.university.dto.response.StudentWithEnrollmentsResponseDTO;
import com.champlain.university.mapper.EnrollmentMapper;
import com.champlain.university.repository.dataaccesslayer.EnrollmentRepository;
import com.champlain.university.service.businesslogiclayer.StudentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;
    private final EnrollmentRepository enrollmentRepository;

    public StudentController(
            StudentService studentService,
            EnrollmentRepository enrollmentRepository
    ) {
        this.studentService = studentService;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> all() {
        return ResponseEntity.ok(studentService.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> one(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(studentService.one(id));
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(
            @Valid @RequestBody StudentRequestDTO dto
    ) {
        StudentResponseDTO result = studentService.create(dto);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO dto
    ) {
        return ResponseEntity.ok(studentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/enrollments")
    public ResponseEntity<StudentWithEnrollmentsResponseDTO> enrollments(
            @PathVariable Long id
    ) {
        StudentResponseDTO student = studentService.one(id);

        var enrollments = enrollmentRepository
                .findByStudentId(id)
                .stream()
                .map(EnrollmentMapper::toDTO)
                .toList();

        return ResponseEntity.ok(
                new StudentWithEnrollmentsResponseDTO(
                        student,
                        enrollments
                )
        );
    }
}

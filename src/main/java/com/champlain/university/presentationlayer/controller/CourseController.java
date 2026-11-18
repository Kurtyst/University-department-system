
package com.champlain.university.controller.presentationlayer;

import com.champlain.university.dto.request.CourseRequestDTO;
import com.champlain.university.dto.response.CourseResponseDTO;
import com.champlain.university.dto.response.CourseWithEnrollmentsResponseDTO;
import com.champlain.university.dto.response.ProfessorResponseDTO;
import com.champlain.university.mapper.EnrollmentMapper;
import com.champlain.university.repository.dataaccesslayer.EnrollmentRepository;
import com.champlain.university.service.businesslogiclayer.CourseService;
import com.champlain.university.service.businesslogiclayer.ProfessorService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final ProfessorService professorService;
    private final EnrollmentRepository enrollmentRepository;

    public CourseController(
            CourseService courseService,
            ProfessorService professorService,
            EnrollmentRepository enrollmentRepository
    ) {
        this.courseService = courseService;
        this.professorService = professorService;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> all() {
        return ResponseEntity.ok(courseService.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> one(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(courseService.one(id));
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> create(
            @Valid @RequestBody CourseRequestDTO dto
    ) {
        CourseResponseDTO result = courseService.create(dto);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody CourseRequestDTO dto
    ) {
        return ResponseEntity.ok(courseService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/enrollments")
    public ResponseEntity<CourseWithEnrollmentsResponseDTO> enrollments(
            @PathVariable Long id
    ) {
        CourseResponseDTO course = courseService.one(id);

        var enrollments = enrollmentRepository
                .findByCourseId(id)
                .stream()
                .map(EnrollmentMapper::toDTO)
                .toList();

        return ResponseEntity.ok(
                new CourseWithEnrollmentsResponseDTO(
                        course,
                        enrollments
                )
        );
    }

    @GetMapping("/{id}/professor")
    public ResponseEntity<ProfessorResponseDTO> professor(
            @PathVariable Long id
    ) {
        CourseResponseDTO course = courseService.one(id);

        return ResponseEntity.ok(
                professorService.one(course.professorId())
        );
    }
}

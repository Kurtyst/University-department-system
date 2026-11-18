
package com.champlain.university.controller.presentationlayer;

import com.champlain.university.dto.request.ProfessorRequestDTO;
import com.champlain.university.dto.response.ProfessorResponseDTO;
import com.champlain.university.dto.response.CourseResponseDTO;
import com.champlain.university.mapper.CourseMapper;
import com.champlain.university.repository.dataaccesslayer.CourseRepository;
import com.champlain.university.service.businesslogiclayer.ProfessorService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    private final ProfessorService professorService;
    private final CourseRepository courseRepository;

    public ProfessorController(
            ProfessorService professorService,
            CourseRepository courseRepository
    ) {
        this.professorService = professorService;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponseDTO>> all() {
        return ResponseEntity.ok(professorService.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> one(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(professorService.one(id));
    }

    @PostMapping
    public ResponseEntity<ProfessorResponseDTO> create(
            @Valid @RequestBody ProfessorRequestDTO dto
    ) {
        ProfessorResponseDTO result = professorService.create(dto);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProfessorRequestDTO dto
    ) {
        return ResponseEntity.ok(professorService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        professorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/courses")
    public ResponseEntity<List<CourseResponseDTO>> courses(
            @PathVariable Long id
    ) {
        professorService.one(id);

        List<CourseResponseDTO> courses = courseRepository
                .findAll()
                .stream()
                .filter(course ->
                        course.getProfessor().getId().equals(id))
                .map(CourseMapper::toDTO)
                .toList();

        return ResponseEntity.ok(courses);
    }
}

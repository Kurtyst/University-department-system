
package com.champlain.university.controller.presentationlayer;

import com.champlain.university.dto.request.EnrollmentRequestDTO;
import com.champlain.university.dto.response.EnrollmentResponseDTO;
import com.champlain.university.service.businesslogiclayer.EnrollmentService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentResponseDTO>> all() {
        return ResponseEntity.ok(enrollmentService.all());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> one(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(enrollmentService.one(id));
    }

    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> create(
            @Valid @RequestBody EnrollmentRequestDTO dto
    ) {
        EnrollmentResponseDTO result = enrollmentService.create(dto);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentRequestDTO dto
    ) {
        return ResponseEntity.ok(enrollmentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

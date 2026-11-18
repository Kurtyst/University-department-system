
package com.champlain.university.service.businesslogiclayer;

import com.champlain.university.domain.domainlayer.Course;
import com.champlain.university.domain.domainlayer.Professor;
import com.champlain.university.dto.request.CourseRequestDTO;
import com.champlain.university.dto.response.CourseResponseDTO;
import com.champlain.university.exception.ConflictException;
import com.champlain.university.exception.NotFoundException;
import com.champlain.university.mapper.CourseMapper;
import com.champlain.university.repository.dataaccesslayer.CourseRepository;
import com.champlain.university.repository.dataaccesslayer.ProfessorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;

    public CourseService(
            CourseRepository courseRepository,
            ProfessorRepository professorRepository
    ) {
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseResponseDTO> all() {
        return courseRepository.findAll()
                .stream()
                .map(CourseMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CourseResponseDTO one(Long id) {
        return CourseMapper.toDTO(findCourse(id));
    }

    public CourseResponseDTO create(CourseRequestDTO dto) {
        if (courseRepository.existsByCode(dto.code())) {
            throw new ConflictException(
                    "Course code already exists"
            );
        }

        Professor professor = findProfessor(dto.professorId());

        Course course = CourseMapper.toEntity(dto, professor);

        return CourseMapper.toDTO(
                courseRepository.save(course)
        );
    }

    public CourseResponseDTO update(
            Long id,
            CourseRequestDTO dto
    ) {
        Course course = findCourse(id);

        courseRepository.findByCode(dto.code())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "Course code already exists"
                    );
                });

        Professor professor = findProfessor(dto.professorId());

        course.setCode(dto.code());
        course.setTitle(dto.title());
        course.setCredits(dto.credits());
        course.setProfessor(professor);

        return CourseMapper.toDTO(
                courseRepository.save(course)
        );
    }

    public void delete(Long id) {
        Course course = findCourse(id);
        courseRepository.delete(course);
    }

    private Course findCourse(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Course not found with ID: " + id
                        )
                );
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

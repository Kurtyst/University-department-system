
package com.champlain.university.repository.dataaccesslayer;

import com.champlain.university.domain.domainlayer.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfessorRepository
        extends JpaRepository<Professor, Long> {

    Optional<Professor> findByEmail(String email);

    boolean existsByEmail(String email);
}

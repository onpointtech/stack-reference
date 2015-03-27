package com.onpoint.repository;

import com.onpoint.domain.Employer;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Employer entity.
 */
public interface EmployerRepository extends JpaRepository<Employer,Long> {

}

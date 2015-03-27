package com.onpoint.repository;

import com.onpoint.domain.ClaimCase;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ClaimCase entity.
 */
public interface ClaimCaseRepository extends JpaRepository<ClaimCase,Long> {

}

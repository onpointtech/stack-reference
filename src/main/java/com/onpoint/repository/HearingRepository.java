package com.onpoint.repository;

import com.onpoint.domain.Hearing;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hearing entity.
 */
public interface HearingRepository extends JpaRepository<Hearing,Long> {

}

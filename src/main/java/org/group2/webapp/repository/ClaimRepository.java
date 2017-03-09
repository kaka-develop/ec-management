package org.group2.webapp.repository;

import org.group2.webapp.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@SuppressWarnings("unused")
public interface ClaimRepository extends JpaRepository<Claim, Long> {

//    List<Claim> findAllByFacultyId(Long id);
}

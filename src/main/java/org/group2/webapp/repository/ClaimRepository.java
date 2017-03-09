package org.group2.webapp.repository;

import org.group2.webapp.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("unused")
public interface ClaimRepository extends JpaRepository<Claim, Long> {

}

package com.ieum.funding.repository;

import com.ieum.funding.domain.Sponsors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepository extends JpaRepository<Sponsors, String> {

}